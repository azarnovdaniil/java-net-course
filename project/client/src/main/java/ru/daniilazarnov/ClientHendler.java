package ru.daniilazarnov;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class ClientHendler {

    private static Socket socket;
    private static ObjectEncoderOutputStream out;
    private static ObjectDecoderInputStream in;

    public static void start() {
        try {
            socket = new Socket("localhost", 8189);
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
            in = new ObjectDecoderInputStream(socket.getInputStream(), 50 * 1024 * 1024);
            ClientHendler network = new ClientHendler();
            network.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initialize() {

        try {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.nextLine();
                String[] commandFile = msg.split("\\s");

                if (msg.startsWith("/")) {
                    if (commandFile[0].equals("/поиск")) {
                        boolean str = (Boolean) in.readObject();
                        System.out.println(str);
                    }
                    if (commandFile[0].equals("/скачать")) {

                        sendMsg(new FileRequest(commandFile[1]));

                        AbstractMessage am = readObject();

                        if (am instanceof FileMessage) {

                            FileMessage fm = (FileMessage) am;
                            Files.write(Paths.get("project", "client", "src", "main", "java", "file/", fm.getFileName()),
                                    fm.getData(), StandardOpenOption.CREATE);
                            System.out.println("скачал");
                        }
                    }

                }

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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


