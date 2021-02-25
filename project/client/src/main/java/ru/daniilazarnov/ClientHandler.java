package ru.daniilazarnov;


import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.File;
import java.io.IOException;

import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.Scanner;

public class ClientHandler {

    private static ObjectEncoderOutputStream out;
    private static ObjectDecoderInputStream in;
    private static final String WAY_CLIENT = ("project/client/src/main/java/file/");
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
            String bossAccount = scannerAccount.nextLine()+("/");
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
                String ServerOrClient = commandFile[0];




                if (msg.equals("/help")) {
                    help();
                }

                if (ServerOrClient.equals("/N")) {

                    String command = commandFile[1];


                    if (command.equals("скачать")) {
                        String file = commandFile[2];
                        download(file);
                    }

                    if (command.equals("отправить")) {
                        String file = commandFile[2];
                        upLoad(file);
                    }

                    if (command.equals("удалить")) {
                        String file = commandFile[2];
                        delete(file,account);
                    }
                    if (command.equals("переименовать")) {
                        renameFile(commandFile, account);
                    }
                }


                if (ServerOrClient.equals("/S")) {
                    String command = commandFile[1];

                    if (command.equals("удалить")) {
                        String file = commandFile[2];

                        deleteFileFromServer(file);

                    }
                    if (command.equals("переименовать")) {

                        String file = commandFile[2];
                        String newFile =  commandFile[3];

                        renameFileFromServer(file, newFile );
                    }
                }


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    public String createAccount(String account) {
        System.out.println("Введите логин");
        if (!Files.exists(Path.of(WAY_CLIENT, account))){
            new File(WAY_CLIENT, account).mkdir();
            System.out.println("Каталог "+ account +" создан");
        } else {
            System.out.println("Вы вошли как " + account);
        }
        initialize(account);

        ;
        return account ;
    }


    private void help() {
        System.out.println("Список команд для клиента:\n/N скачать ИмяФайла.формат"
                + " \n/N отправить ИмяФайла.формат\n/N удалить ИмяФайла.формат\n"
                + "/N переименовать ИмяФайла.формат НовоеИмяФайла.формат");

        System.out.println("\nСписок команд для сервера:\n/S удалить ИмяФайла.формат\n"
                + "/S переименовать ИмяФайла.формат НовоеИмяФайла.формат");
    }

    public void renameFile(String[] messageCommand, String bossAccount) {
        String oldNameFile = messageCommand[2];
        String newNameFile = messageCommand[3];

        File file = new File(WAY_CLIENT + bossAccount, oldNameFile);
        File newFile = new File(WAY_CLIENT + bossAccount, newNameFile);
        if (file.renameTo(newFile)) {
            System.out.println("Файл " + file + " успешно переименован в " + newFile);
        } else {
            System.out.println("Файл " + file + " НЕ переименован в " + newFile);
        }
    }

    private void renameFileFromServer(String s, String s1) {

        try {
            sendMesg("/переименовать" + " " + s + " " + s1);
            AbstractMessage am = readObject();
            MyMessage fm = (MyMessage) am;
            System.out.println(fm.getMyMessage());

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }


    private static void delete(String file,  String bossAccount) {

        try {
            Path deletePath = Paths.get(WAY_CLIENT+ bossAccount +file);
            Files.delete(deletePath);
            System.out.println("Файл " + file + " успешно удален у вас");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void deleteFileFromServer(String s) throws ClassNotFoundException, IOException {
        sendMesg("/удалить " + s);
        AbstractMessage am = readObject();
        if (am instanceof MyMessage) {
            MyMessage fm = (MyMessage) am;
            System.out.println(fm.getMyMessage());
        }
    }

    private boolean sendMesg(String msg) {
        try {
            out.writeObject(msg);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void upLoad(String file) {
        try {
            FileMessage fm = new FileMessage(Paths.get(WAY_CLIENT + file));
            out.writeObject(fm);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("upLoad " + file);

    }


    public static void download(String file) throws IOException, ClassNotFoundException {
        sendMsg(new FileRequest(file));

        AbstractMessage am = readObject();

        if (am instanceof FileMessage) {

            FileMessage fm = (FileMessage) am;
            Files.write(Paths.get(WAY_CLIENT, fm.getFileName()), fm.getData(), StandardOpenOption.CREATE);
            System.out.println("download " + file);
        }

    }


    // получение сообщения с сервака----------------------------------------------------------
    public static AbstractMessage readObject() throws ClassNotFoundException, IOException {
        Object obj = in.readObject();
        return (AbstractMessage) obj;
    }


    //отправка сообщения на сервак------------------------------------------------------------
    public static boolean sendMsg(AbstractMessage msg) {
        try {
            out.writeObject(msg);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}


