package client;

import java.nio.file.Files;
import java.nio.file.Path;

public class ServerNetwork {
    private static Network client;
    private static String userFolder = "src/main/java/server/fileUser1//";


    static void downloadFile(String inputLine) {
        if (isThereaSecondElement(inputLine)) {
            String command = getSecondElement(inputLine);
            sendStringAndCommandByte(command, (byte) 3);
        } else {
            System.out.println();
        }
    }
    static void sendFile(String inputLine) {
        if (isThereaSecondElement(inputLine)) {
            String fileName = getSecondElement(inputLine);
            if (isFileExists(fileName)) {
                client.sendFile(userFolder);

            } else {
                System.out.println("Файл не найден");
            }
        }
    }
    public static boolean isThereaSecondElement(String inputLine) {
        return inputLine.split(" ").length == 2;
    }
    public static String getSecondElement(String inputLine) {
        return inputLine.split(" ")[1];
    }
    public static void sendStringAndCommandByte(String folderName, byte commandByte) {
        client.sendStringAndCommand(folderName, commandByte);
    }
    private static boolean isFileExists(String fileName) {
        return Files.exists(Path.of(fileName));
    }

}
