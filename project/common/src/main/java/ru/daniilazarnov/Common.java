package ru.daniilazarnov;

import ru.daniilazarnov.domain.FileMessage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;


public class Common {
    public Common() {
    }

    public FileMessage sendFile(String fileName, Path path) throws IOException {
        byte[] fileContent = Files.readAllBytes(path);
        return new FileMessage(fileName, fileContent);
    }

    public void receiveFile(Object msg, String directory) {
        try {
            Path path = Path.of(directory + "\\" + ((FileMessage) msg).getFileName());
            if (Files.exists(path)) {
                Files.delete(path);
            }
            Files.write(path, ((FileMessage) msg).getContent(), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String showFiles(String path) {
        File dir = new File(path);
        StringBuilder sb = new StringBuilder();

        File[] files = dir.listFiles();
        for (File file : files) {
            long lastModified = file.lastModified();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            sb.append(file.getName() + "\n\r");
        }
        return sb.toString();
    }

    public String deleteFile(String address, String src) {
        address = src + "\\" + address;
        try {
            Files.delete(Path.of(address));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String("File was deleted");
    }

    public String renameFile(String lastName, String newName, String src) {
        String addressNew = src + "\\" + newName;
        if (Files.notExists(Path.of(addressNew))) {
            File file = new File(lastName);
            File newFile = new File(newName);
            file.renameTo(newFile);
            return new String("File was renamed");
        } else {
            return new String("File with name " + newName + "already exist");
        }
    }
}
