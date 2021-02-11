package com.geekbrains.dbox.client;

import com.geekbrains.dbox.client.WorkFiles.WorkDir;
import com.geekbrains.dbox.client.WorkFiles.WorkFileBytes;
import com.geekbrains.dbox.server.user.User;

import java.io.*;

public class Client {
   // static public Message inMessage = null;
    static public int userId = -1;
    static public String userLogin = "";
    static public String userFIO = "";

    public static void main(String[] args) throws Exception {
        Network network = new Network();


        InputStream inputStream = System.in;
        Reader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        boolean t = true;
        String[] sAr = new String[2];
        Message m = new Message();

        String command = "";




        int tempIndexAth = 0;
        while (t){
//            if (inMessage != null) {
//                if (inMessage.publiic) { // Выводим информационные сообщения если они есть
//                    System.out.println((String) inMessage.obj);
//                    inMessage.publiic = false;
//                    inMessage.obj = "";
//                }
//            }

            if (userId == -1) { // Авторизация
                if (tempIndexAth == 0) {
                    System.out.print("Login: ");
                    sAr[0] = bufferedReader.readLine(); //читаем строку с клавиатуры
                    System.out.print("Password: ");
                    sAr[1] = bufferedReader.readLine(); //читаем строку с клавиатуры

                    m.com = 5;
                    m.obj = sAr;
                    tempIndexAth = 1;
                }
                network.sendMessage(m);
                Thread.sleep(30);
            }if (userId == -2) {// Сервер ответил отказом авторизации
                    tempIndexAth = 0;
                    userId = -1;
            }else{
                //inMessage.publiic = false;
                System.out.println("Help - помощь");
                command = bufferedReader.readLine();
                String[] arrStr = command.split(" ", 2);

                switch (arrStr[0]){
                    case "Help":
                        System.out.println("ls - папка");
                        System.out.println("link_dir - связать папку с DBOX");
                        System.out.println("link_file - связать файл с DBOX");
                        break;
                    case "ls":
                        WorkDir wD = new WorkDir();
                        wD.setDirRoot(arrStr[1]);
                        String[] arr = wD.getAll("");

                        for (int i = 0; i < arr.length; i++) {
                            System.out.println(arr[i]);
                        }
                        break;
                    case "link_dir":
                        break;
                    case "link_file":
                        m.com = 8;

                        WorkFileBytes wB = new WorkFileBytes(arrStr[1].trim());
                        String md5 = wB.getCheckSummMD5(); ///////////////Считываем контрольную сумму

                        Object[] nested = new Object[3];
                        nested[0] = 1;
                        nested[1] = arrStr[1];
                        nested[2] = md5;

                        m.obj = nested;

                        network.sendMessage(m);
                        break;
                }

               // Thread.sleep(3000);
            }
        }

    }

}
