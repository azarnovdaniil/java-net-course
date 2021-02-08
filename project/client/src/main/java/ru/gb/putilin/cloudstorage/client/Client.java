package ru.gb.putilin.cloudstorage.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {

    private final FileManager fileManager;

    public Client(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void run() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputString;
        while ((inputString = br.readLine()) != null) {
            int whitespaceIndex = inputString.indexOf(" ");
            try {
                if (whitespaceIndex != -1) {
                    String command = inputString.substring(0, whitespaceIndex);
                    System.out.println(command);
                    if (command.equals(ClientCommand.DOWNLOAD.getTitle())) {
                        String pathString = inputString.substring(whitespaceIndex + 1);
                        fileManager.downloadFile(pathString);
                    } else if (command.equals(ClientCommand.UPLOAD.getTitle())) {
                        String pathString = inputString.substring(whitespaceIndex + 1);
                        fileManager.uploadFile(pathString);
                    } else if (command.equals(ClientCommand.SHOW.getTitle())) {
                        fileManager.showFiles();
                    } else {
                        throw new IllegalArgumentException("Unknown command.");
                    }
                } else {
                    throw new IllegalArgumentException("Unknown command.");
                }
            } catch (IllegalArgumentException exception) {
                System.out.println(exception);
            }
        }
    }

}
