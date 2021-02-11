package com.geekbrains.dbox.server.handlers.in;

import com.geekbrains.dbox.WorkFiles.Data_File.WorkFileLinesMod;
import com.geekbrains.dbox.WorkFiles.WorkFileBytes;
import com.geekbrains.dbox.server.ServerApp;
import com.geekbrains.dbox.server.comands.Comand;
import com.geekbrains.dbox.server.comands.Comands;
import com.geekbrains.dbox.server.comands.Separator;
import com.geekbrains.dbox.server.files.AllLinksFiles;
import com.geekbrains.dbox.server.files.LinkFile;
import com.geekbrains.dbox.server.handlers.Message;
import com.geekbrains.dbox.server.user.User;
import com.geekbrains.dbox.server.user.Users;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.util.ArrayList;

import static java.io.File.separatorChar;

public class HandlerInSwitchComand extends ChannelInboundHandlerAdapter {
    private static Users listUsers = new Users();
    private static AllLinksFiles allLinks = new AllLinksFiles();
    private User thisUser = null;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //Подключение клиента
        //System.out.println("HandlerIn2");
    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        listUsers.add("user","user","useraaa");
       // new Thread(() -> {
            Comands cm = new Comands("C:\\Users\\admin\\IdeaProjects\\DBOX");
            int idUser = -1;

//                byte[] bytes = (byte[]) msg;
//                byte[] newB = new byte[bytes.length - 1];
//                byte comand = bytes[0];
//                for (int i = 1; i < bytes.length; i++) {
//                    newB[i - 1] = bytes[i];
//                }
        Message inM  = (Message) msg;
        Message outM  = (Message) msg;
        //System.out.println(">>> " + m.com);

                Comand com = null;

                switch (inM.com){
                    case 1: //ls

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:
                        com = Comand.Auth;//////////////////////////////////////////////Авторизация
                        ////////////////////////////////////////Отправка информации по авторизации
                        String[] sa2 = (String[]) inM.obj;
                        String mess = "";

                        int[] res = listUsers.auth(sa2[0], sa2[1]);
                        outM.com = 5; //Информационное сообщение на клиент

                        if (res[0] == 1) {
                            idUser = res[1];
                            thisUser = listUsers.getUser_Id(idUser);
                            mess = "Добро пожаловать " + listUsers.getUser_Id(idUser).getFio();
                            System.out.println(">> " + mess);
                            outM.obj = mess;
                        }else {
                            // возвращаем ошибку
                            String ss = "";
                            if(res[1] == 0) ss = "логин";
                            else ss = "пароль";
                            mess = "Ошибка авторизации \b " + ss + " не верен" ;
                            outM.obj = mess;
                            }

                        ctx.channel().writeAndFlush(outM);
                        Thread.sleep(1000);
                        ///////////////////////////////////////Отправка данных авторизованного пользователя
                        if (res[0] == 1) {
                            outM.com = 6;
                            String[] arrStr = new String[3];
                            arrStr[0] = thisUser.getLogin();// Загружаем Логин
                            arrStr[1] = thisUser.getFio();// Загружаем ФИО
                            arrStr[2] = Integer.toString(idUser);//Загружаем id пользователя
                            outM.obj = arrStr;//Отправляем
                        }else{
                            outM.com = 7;
                            outM.obj = "";
                        }

                        ctx.channel().writeAndFlush(outM);
                        break;
                    case 8: ////////////////////////////////Создание Link на файл
                        Object[] nested = (Object[]) inM.obj;
                        int indexPack = (int) nested[0];
                        String pathReal = (String) nested[1];
                        String md5 = (String) nested[2];

                        boolean result1 = allLinks.add(thisUser.getId(), thisUser.getLogin(), pathReal, md5);
                        outM.com = 5;
                        if (result1) outM.obj = "Ссылка добавлена";
                        else outM.obj = "Ссылка уже существует";

                        ctx.channel().writeAndFlush(outM);

                        linkFile_load(ctx, allLinks.getLinkUser(thisUser.getId(), pathReal));

                        allLinks.print();
                        break;
                    case 9:
                        System.out.println("9:");
                        System.out.println("Obj - " + inM.obj.toString());
                        Object[] nested1 = (Object[]) inM.obj;

                        // 0 - индекс работы с файлом
                        // 1 - путь до файла
                        // 2 - контрольная сумма
                        // 3 - данные файла

                        if((int)nested1[0] == 0){                                                //Файла нет
                            System.out.println("9:0");
                        allLinks.del(allLinks.getLinkUser(thisUser.getId(),(String) nested1[1]));//Удаление ссылки на файл
                        }

                        if(((int)nested1[0] == 1)|((int)nested1[0] == 2)){    //Первый пакет файла [0] - индекс передачи файла [1] - int (контрольная сумма)
                            System.out.println("9:1");
                            String pathToFile = (String) nested1[1];
                            int checkSumm = (int)nested1[2];
                            if (checkSumm == checkSum((byte[])nested1[3])){
                                System.out.println("Chek true");
                                int sizePack = ((byte[]) nested1[3]).length;
                                LinkFile lf = allLinks.getLinkUser(thisUser.getId(), pathToFile);
                                lf.colPak =+ 1;

                                WorkFileBytes wb =  new WorkFileBytes(thisUser.getLogin() + separatorChar + lf.getNikname());
                                wb.setModWriter(WorkFileLinesMod.DOWN);
                                wb.writerInFile(nested1[3]);

                                //Ответное смс об удачной доставке
                                outM.com = 9;
                                Object[] o1 = new Object[3];
                                o1[0] = 2;
                                o1[1] = pathToFile;
                                o1[2] = lf.colPak;

                                if ((int)nested1[0] == 2){
                                    lf.colPak = 0;
                                    o1[2] = -1;
                                }
                                outM.obj = o1;
                                ctx.channel().writeAndFlush(outM);

                            }else{
                                System.out.println("Chek false");
                            }
                        }
                        break;
                }
       // }).start();

    }

    @Override
    //Возникновение исключения
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //Вывод исключения в консоль
        cause.printStackTrace();
        //закрытие соединения
        ctx.close();

    }

    private void linkFile_load(ChannelHandlerContext ctx, LinkFile lf){
        Message outM = new Message();
        outM.com = 9;
        Object[] res = new Object[2];
        res[0] = 0;                         ///Инициируем загрузку файла
        res[1] = lf.getRealName();

        outM.obj = res;
        ctx.channel().writeAndFlush(outM);
    }

    private boolean checkUser(int idUser){
        if (idUser == -1) return false;
        else return true;
    }
    public String toString (byte[] b){
        String str = "";
        for (int i = 0; i < b.length; i++) {
            str += (char) b[i];
        }
        return str;
    }
    public String[] toArrString_SepString(String s) {
        return s.split(Separator.stringSep.getCode());
    }
    public int checkSum(byte[] input) {
        int checkSum = 0;
        for(byte b : input) {
            checkSum += b & 0xFF;
        }
        return checkSum;
    }
}
