package ru.daniilazarnov;


import commands.RenameFiles;
import commands.UpLoad;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.File;
import java.io.IOException;

import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Scanner;

import static commands.DeleteFile.delete;
import static commands.DeleteFile.deleteFileFromServer;
import static commands.DownLoad.download;
import static commands.RenameFiles.renameFile;

public class ClientHandler {

    protected static ObjectEncoderOutputStream out;
    protected static ObjectDecoderInputStream in;
    protected static final String WAY_CLIENT = ("project/client/src/main/java/file/");
    private static final int PORT = 8189;
    private static final int SIZE_1024 = 1024;
    private static final int SIZE_100 = 100;



    public static void start() {
        try {
            Socket socket = new Socket("localhost", PORT);
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
            in = new ObjectDecoderInputStream(socket.getInputStream(), SIZE_100 * SIZE_1024 * SIZE_1024);
            ClientHandler network = new ClientHandler();

            System.out.println("Ведите логин");
            Scanner scannerAccount = new Scanner(System.in);
            String bossAccount = scannerAccount.nextLine() + ("/");
            network.createAccount(bossAccount);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initialize(String account) {



        while (true) {

            Scanner scannerMsg = new Scanner(System.in);
            String msg = scannerMsg.nextLine();

            try {

                String[] commandFile = msg.split("\\s");
                String serverOrClient = commandFile[0];




                if (msg.equals("/help")) {
                    help();
                }

                if (serverOrClient.equals("/N")) {

                    String command = commandFile[1];


                    if (command.equals("скачать")) {
                        String file = commandFile[2];
                        download(file, account);
                    }

                    if (command.equals("отправить")) {
                        String file = commandFile[2];
                        UpLoad.upLoad(file, account);
                    }

                    if (command.equals("удалить")) {
                        String file = commandFile[2];
                        delete(file, account);
                    }
                    if (command.equals("переименовать")) {

                        renameFile(commandFile, account);
                    }
                }


                if (serverOrClient.equals("/S")) {
                    String command = commandFile[1];

                    if (command.equals("удалить")) {
                        String file = commandFile[2];

                        deleteFileFromServer(file, account);

                    }
                    if (command.equals("переименовать")) {

                        String file = commandFile[2];
                        String newFile =  commandFile[3];

                        RenameFiles.renameFileFromServer(file, newFile, account);
                    }
                }


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    public String createAccount(String account) {
        System.out.println("Введите логин");
        if (!Files.exists(Path.of(WAY_CLIENT, account))) {
            new File(WAY_CLIENT, account).mkdir();
            System.out.println("Каталог " + account + " создан");
        } else {
            System.out.println("Вы вошли как " + account);
        }
        ClientHandler.sendMessage("/создать " + account);

        initialize(account);


        return account;
    }


    private void help() {
        System.out.println("Список команд для клиента:\n/N скачать ИмяФайла.формат"
                + " \n/N отправить Имя Файла.формат\n/N удалить Имя Файла.формат\n"
                + "/N переименовать Имя Файла.формат Новое Имя Файла.формат");

        System.out.println("\nСписок команд для сервера:\n/S удалить Имя Файла.формат\n"
                + "/S переименовать Имя Файла.формат Новое Имя Файла.формат");
    }

    // получение сообщения с сервака----------------------------------------------------------
    public static AbstractMessage readObject() throws ClassNotFoundException, IOException {
        Object obj = in.readObject();
        return (AbstractMessage) obj;
    }


    //отправка сообщения на сервак------------------------------------------------------------
    public static boolean sendMsgFromDownload(AbstractMessage msg) {
        try {
            out.writeObject(msg);
            return true;
        } catch (IOException e) {
            System.out.println("не удалось скачать");
        }
        return false;
    }

    protected static void sendMessage(String msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            System.err.println("Не удалось отправить запрос");
        }
    }
}


