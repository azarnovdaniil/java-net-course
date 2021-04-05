package ru.daniilazarnov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Client {
    private static final String USER = "User1";
    private static final String USER_FOLDER = "src/main/java/server/";
    private final Network network;

    public Client(Network network) {
        this.network = network;
        network.start();
    }


    private void run() throws IOException {
        System.out.println("Hello, " + USER + "! Type \"help\" for help");

//        while (true) {
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//            String inputLine = bufferedReader.readLine().trim().toLowerCase();
//            Commands commands = Commands.valueOf(inputLine.split("\\n")[0]);
//            switch (commands) {
//                case UPLOAD:
//                    ServerNetwork.sendFile(inputLine);
//                    break;
//                case DOWNLOAD:
//                    ServerNetwork.downloadFile(inputLine);
//                    break;
//                case HELP:
//                    System.out.println(commands.helpInfo());
//                    break;
//                default:
//                    throw new IllegalStateException("Invalid command: " + commands);
//            }
//        }
    }
    protected static void walk() {
        try (Stream<Path> stream = Files.walk(Path.of(USER_FOLDER))) {
            System.out.println("Content of your folder:");
            stream.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        }
    }

    public static void main(String[] args) throws IOException {
        Network network = new Network();
        new Client(network).run();
    }
}
