package ru.daniilazarnov;

import java.nio.file.Files;
import java.nio.file.Path;

public class ServerNetwork {
    private static Network network;
    private static final String USER_FOLDER = "src/main/java/server/";

    public ServerNetwork() {
    }

    static void downloadFile(String inputLine) {
        if (isThereaSecondElement(inputLine)) {
            String command = getSecondElement(inputLine);
            sendStringAndCommandByte(command, Commands.DOWNLOAD.getCommBytes());
        } else {
            System.out.println();
        }
    }
    static void sendFile(String inputLine) {
        if (isThereaSecondElement(inputLine)) {
            String fileName = getSecondElement(inputLine);
            if (isFileExists(fileName)) {
                network.sendFile(USER_FOLDER);

            } else {
                System.out.println("File not found");
            }
        }
    }
    public static boolean isThereaSecondElement(String inputLine) {
        return inputLine.split("\\n").length == 2;
    }
    public static String getSecondElement(String inputLine) {
        return inputLine.split("\\n")[1];
    }
    public static void sendStringAndCommandByte(String folderName, byte commandByte) {
        network.sendStringAndCommand(folderName, commandByte);
    }
    private static boolean isFileExists(String fileName) {
        return Files.exists(Path.of(fileName));
    }

}

