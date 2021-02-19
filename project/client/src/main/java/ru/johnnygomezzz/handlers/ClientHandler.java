package ru.johnnygomezzz.handlers;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import ru.johnnygomezzz.AbstractMessage;
import ru.johnnygomezzz.FileMessage;
import ru.johnnygomezzz.FileRequest;

import java.io.IOException;

import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class ClientHandler {

    private static ObjectEncoderOutputStream out;
    private static ObjectDecoderInputStream in;
    private static final int PORT = 8189;
    private static final int SIZE_1024 = 1024;
    private static final int SIZE_100 = 100;
    private static final String WAY = ("project/client/src/main/java/file/");

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
                            Files.write(Paths.get(WAY, fm.getFileName()), fm.getData(), StandardOpenOption.CREATE);
                            System.out.println("скачал");
                        }
                    }
                    if (commandFile[0].equals("/отправить")) {
                        FileMessage fm = new FileMessage(Paths.get(WAY + commandFile[1]));
                        out.writeObject(fm);
                        out.flush();
                        System.out.println("отправил " + commandFile[1]);
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
