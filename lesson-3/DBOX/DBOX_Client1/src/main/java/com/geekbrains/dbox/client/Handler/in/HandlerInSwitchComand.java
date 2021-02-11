package com.geekbrains.dbox.client.Handler.in;


import com.geekbrains.dbox.client.*;
import com.geekbrains.dbox.client.WorkFiles.WorkFileBytes;
import com.geekbrains.dbox.server.comands.Separator;
import com.geekbrains.dbox.server.user.Users;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HandlerInSwitchComand extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //Подключение клиента
        //System.out.println("HandlerIn2");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       // new Thread(() -> {
            Comands cm = new Comands("C:\\Users\\admin\\IdeaProjects\\DBOX");
            int idUser = -1;

//                byte[] bytes = (byte[]) msg;
//                byte[] newB = new byte[bytes.length - 1];
//                byte comand = bytes[0];
//                for (int i = 1; i < bytes.length; i++) {
//                    newB[i - 1] = bytes[i];
//                }
        Message m  = (Message) msg;
        Message outM = new Message();
        //System.out.println(">>> " + m.com);

                Comand com = null;

                switch (m.com){
                    case 1: //ls
                        if (checkUser(idUser)) {
                            // возвращаем
                            break;
                        }
                        com = Comand.ls;
                        String str1 = (String) m.obj;
                        String[] s = cm.comand_Ls(str1);

                        // s - возвращаем

                        break;
                    case 2:
                        if (checkUser(idUser)) {
                            // возвращаем
                            break;
                        }
                        com = Comand.createDir;
                        String str2 = (String) m.obj;
                        boolean res1 = cm.comand_NewDir(str2);
                        // res1 - возвращаем

                        break;
                    case 3:
                        if (checkUser(idUser)) {
                            // возвращаем
                            break;
                        }
                        com = Comand.delete;
                        String str3 = (String) m.obj;
                        boolean res2 = cm.comand_Delete(str3);
                        // res2 - возвращаем

                        break;
                    case 4:
                        if (checkUser(idUser)) {
                            // возвращаем
                            break;
                        }
                        com = Comand.rename;
                        String[] sa1 = (String[]) m.obj;

                        boolean res3 = cm.comand_Rename(sa1[0], sa1[1]);
                        // res3 - возвращаем

                        break;
                    case 5: // Информационное сообщение
//                        com = Comand.Auth;//////////////////////////////////////////////Авторизация
                        System.out.println((String) m.obj);

                        break;
                    case 6: // Логин и ФИО пользователя
                        String [] arrStr = (String[]) m.obj;
                        Client.userLogin = arrStr[0];
                        Client.userFIO = arrStr[1];
                        Client.userId = Integer.parseInt(arrStr[2]);
                        break;
                    case 7:
                        Client.userId = -2;
                        break;
                    case 9:                                     ///Работа с файлом
                        Object[] inPak = (Object[]) m.obj;
                        if(((int)inPak[0] == 0)|((int)inPak[0] == 2)) {                ///Запрос на загрузку файла
                            WorkFileBytes wb = new WorkFileBytes((String) inPak[1]);

                            if (wb.isFile()) { //Файл существует
                                //Запрашиваем размер файл
                                //Разбиваем на количество пакетов по длинне пакета
                                //в цикле передаем частями файл
                                ///каждый пакет сопроваждаем контрольной суммой.
                                // ждем подтверждения получения пакета от сервера

                                outM.com = 9;
                                Object[] pak = new Object[4];
                                // 0 - индекс работы с файлом
                                // 1 - путь до файла
                                // 2 - контрольная сумма
                                // 3 - данные файла
                                pak[1] = inPak[1];
                                int oldStep = 0;

                                if((int)inPak[0] == 2) {
                                    if((int)inPak[2] == -1){
                                        System.out.println((String) inPak[1] + " Передан");
                                    }
                                    oldStep = (int) inPak[2];
                                }

                                    int sizePak = 500;                                          //Длинна одного пакета в байтах

                                long size = wb.getSizeFile();
                                int step = (int) Math.ceil(size / sizePak);

                                if((sizePak * oldStep + sizePak) < size) pak[0] = 1;//Передача файла
                                else pak[0] = 2;//Передача файла (последний пакет)

                                wb.setSizeReadeArrByte(sizePak);
                                pak[3] = wb.readInFile(sizePak * oldStep);
                                pak[2] = checkSum((byte[]) pak[3]);

                                outM.obj = pak;
                                ctx.channel().writeAndFlush(outM);

                                System.out.println("Pak - " + ((byte[]) pak[3]).length);
                                System.out.println("PakSumm - " + ((int) pak[2]));
                                System.out.println("Size\\Step - " + size + "\\" + step);
                                // System.out.println("Obj - " + outM.obj.toString());

                                }else{   /// Если файла нет
                                outM.com = 9;
                                Object[] out = new Object[2];
                                out[0] = 0; // файла нет
                                out[1] = inPak[1];
                                ctx.channel().writeAndFlush(outM);
                            }
                        }
                        break;
                }

        m = null;
    }

    @Override
    //Возникновение исключения
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //Вывод исключения в консоль
        cause.printStackTrace();
        //закрытие соединения
        ctx.close();

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
