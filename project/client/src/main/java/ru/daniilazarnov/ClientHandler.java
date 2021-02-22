package ru.daniilazarnov;


import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;

import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
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
            network.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initialize() {
        System.out.println("Список команд:\n/скачать ИмяФайла.формат" +
                " \n/отправить ИмяФайла.формат \n/удалить ИмяФайла.формат (удаляет на сервере)");
        try {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.nextLine();
                String[] commandFile = msg.split("\\s");


                if (commandFile[0].equals("/скачать")) {
                    download(commandFile[1]);
                }

                if (commandFile[0].equals("/отправить")) {
                    upLoad(commandFile[1]);
                }
                if (commandFile[0].equals("/удалить")) {
                    sendMesg("/удалить " + commandFile[1]);
                    AbstractMessage am = readObject();
                    if (am instanceof MyMessage) {
                        MyMessage fm = (MyMessage) am;
                        System.out.println((fm).getMyMessage());
                    }
                }


            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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


