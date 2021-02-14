package ru.daniilazarnov;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Client {
    private static Network clientNetwork;

    static final String HOME_FOLDER_PATH = "project/client/local_storage/";

    public static void main(String[] args) {

        init();
        try {
            mainHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void mainHandler() throws IOException {
        String inputLine;
        while (true) {
            InputStream in = System.in;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            if (clientNetwork.isConnect()) {

                inputLine = bufferedReader.readLine().trim().toLowerCase();
                String firstCommand = inputLine.split(" ")[0];

                Commands commands = Commands.valueOf(firstCommand);


                switch (commands) {
                    case ULF:
                        sendCommand(inputLine);
                        break;
                    case FLS:
                        System.out.println(commands.getFilesListFromLocalDirectory(inputLine));
                        break;
                    case EXIT:
                        clientNetwork.close();
                        break;
                    case DLF:
                        sendNameFIleForDownloading(inputLine);
                        break;
                    case HELP:
                        System.out.println(commands.help());
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + inputLine);
                }
            }
        }
    }

    private static void sendNameFIleForDownloading(String inputLine) {

    }

    private static void init() {
        clientNetwork = new Network();
    }

    public static void sendCommand(String inputLine) {
        if (inputLine.split(" ").length == 2) {
            String fileName = inputLine.split(" ")[1];
            if (isFileExists(fileName)) {
                clientNetwork.sendFile(HOME_FOLDER_PATH + fileName);
            } else {
                System.out.println("File not founded");
            }
        } else {
            System.out.println("local_storage: некорректный аргумент");
        }
    }

    private static boolean isFileExists(String fileName) {
        return Files.exists(Path.of(HOME_FOLDER_PATH + fileName));

    }
}
