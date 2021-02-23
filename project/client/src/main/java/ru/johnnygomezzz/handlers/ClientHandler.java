package ru.johnnygomezzz.handlers;


import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import ru.johnnygomezzz.AbstractMessage;
import ru.johnnygomezzz.FileMessage;
import ru.johnnygomezzz.FileRequest;
import ru.johnnygomezzz.MyMessage;
import ru.johnnygomezzz.commands.*;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Scanner;

public class ClientHandler {

    private static ObjectEncoderOutputStream out;
    private static ObjectDecoderInputStream in;
    private static final String HOST = "localhost";
    private static final int PORT = 8189;
    private static final int SIZE = 100 * 1024 * 1024;
    private static final String PATH_LOCAL = ("project/client/local/");

    public static void start() {
        try {
            Socket socket = new Socket(HOST, PORT);
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
            in = new ObjectDecoderInputStream(socket.getInputStream(), SIZE);
            ClientHandler network = new ClientHandler();

            System.out.println("Добро пожаловать в Хранилище №13!\n/help для подсказки.\n\n"
                    + "Введите команду:");

            network.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {

        try {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();
                String[] messagePart = message.split("\\s");

                if (message.startsWith(Commands.HELP.getName())) {
                    new HelpCommand().printHelp();

                } else if (message.startsWith(Commands.QUIT.getName())) {
                    MyMessage msg = new MyMessage(messagePart[0]);
                    out.writeObject(msg);
                    out.flush();
                    new QuitCommand().quit();

                } else if (message.startsWith(Commands.LS.getName()) && messagePart.length > 1) {
                    new ListCommand().listCommand(messagePart[1]);

                } else if (message.startsWith(Commands.TOUCH.getName()) && messagePart.length > 1) {
                    new TouchCommand().touchCommand(messagePart[1], messagePart[2], messagePart.length);

                } else if (message.startsWith(Commands.DOWNLOAD.getName()) && messagePart.length > 1) {
                    sendMsg(new FileRequest(messagePart[1]));
                    AbstractMessage am = readObject();
                    new DownloadCommand().downloadCommand(am);

                } else if (message.startsWith(Commands.UPLOAD.getName()) && messagePart.length > 1) {
                    FileMessage fm = new FileMessage(Paths.get(PATH_LOCAL + messagePart[1]));
                    out.writeObject(fm);
                    out.flush();
                    new UploadCommand().uploadCommand(fm);

                } else if (message.startsWith(Commands.MKDIR.getName()) && messagePart.length > 1) {
                    new MkDirCommand().mkDirCommand(messagePart[1]);

                } else if (message.startsWith(Commands.DELETE.getName()) && messagePart.length > 1) {
                    new DeleteCommand().deleteCommand(messagePart[1]);

                } else {
                    System.out.println("\"" + message + "\""
                            + " неполное значение или не является командой.\nВведите команду:");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static AbstractMessage readObject() throws ClassNotFoundException, IOException {
        Object obj = in.readObject();
        return (AbstractMessage) obj;
    }

    public static void sendMsg(AbstractMessage msg) {
        try {
            out.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


