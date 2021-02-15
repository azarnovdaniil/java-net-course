package client;

import common.Commands;
import common.FileSender;
import io.netty.channel.ChannelHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.stream.Stream;

public class Client {
    private static String user = "User1";
    private static String userFolder = "src/main/java/server/fileUser1";



    public static void main(String[] args) throws IOException {
        mainHandler();
    }
    private static void mainHandler() throws IOException {
        System.out.println(" Здравствуйте " + user + "\n наберите help для получения справки");
        Network net = new Network();
        ServerNetwork servNet = new ServerNetwork();

        String inputLine;
        while (true) {
            InputStream in = System.in;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            if (net.isConnect()) {
                while (true) {
                    if (!FileSender.isLoadingStatus()) {
                        break;
                    }
                }
                inputLine = bufferedReader.readLine().trim().toLowerCase();
                String firstCommand = inputLine.split(" ")[0];
                Commands commands;
                try {
                    commands = Commands.valueOf(firstCommand.toUpperCase());
                    switch (commands) {
                        case UPLOAD:
                            servNet.sendFile(inputLine);
                            break;
                        case DOWNLOAD:
                            servNet.downloadFile(inputLine);
                            break;
                        case DELETE:
                            break;
                        case RENAME:
                            break;
                        case LS:
                            walk();
                            break;
                        case HELP:
                            System.out.println(commands.helpInfo());
                            break;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Нет такой команды");
                    mainHandler();
                }
            }
        }
    }
    protected static void walk() {
        Stream<Path> stream;
        try {
            stream = Files.walk(Path.of(userFolder));
            System.out.println("Содержимое вашего хранилища:");
            stream.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        }

    }
}
