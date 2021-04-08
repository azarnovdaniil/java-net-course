package ru.daniilazarnov;

import ru.daniilazarnov.domain.FileMessage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final int DEFAULT_PORT = 8189;
    public static final String DEFAULT_HOST = "localhost";
    public Common(){}

    public FileMessage sendFile (String fileName, Path path) throws IOException {
        byte[] fileContent = Files.readAllBytes(path);
        return new FileMessage(fileName, (int) Files.size(path), fileContent );
    }
    public void receiveFile (Object msg, String directory) throws IOException {
        try {
            Path path = Path.of(directory + "\\" + ((FileMessage) msg).getFileName());
            if(Files.exists(path)){ Files.delete(path);}
            Files.write(path, ((FileMessage) msg).getContent(), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public StringBuilder showFiles (String path) {
        File dir = new File(path);
        StringBuilder sb = new StringBuilder();

        File[] files = dir.listFiles();
        sb.append(" For user: user " + "\n");
        for (File file : files) {
            long lastModified = file.lastModified();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            sb.append(file.getName() + " ,date of change " + sdf.format(new Date(lastModified)) + "\n");
        }
        return sb;
    }

    public String deleteFile (String address, String src) {
        address = src + "\\" + address;
        try {
            Files.delete(Path.of(address));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String("File was deleted");
    }
    public String renameFile (String lastName, String newName, String src) {
        String s;
        String address = src + "\\" + lastName;
        String addressNew = src + "\\" + newName;
        if (Files.notExists(Path.of(addressNew))){
            File file = new File(lastName);
            File newFile = new File(newName);
            file.renameTo(newFile);
            return s= "File was renamed";
        }else {
            return s= "File with name " + newName+ "already exist";
        }
    }
}
