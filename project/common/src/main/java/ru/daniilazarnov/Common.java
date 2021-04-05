package ru.daniilazarnov;

import ru.daniilazarnov.domain.FileMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Common {
    public static final int DEFAULT_PORT = 8189;
    public static final String DEFAULT_HOST = "localhost";
    private Integer port;
    private String host;
    public Common(){}

    public FileMessage sendFile (String fileName, Path path) throws IOException {
        byte[] fileContent = Files.readAllBytes(path);
        FileMessage fileMessage = new FileMessage(fileName, (int) Files.size(path), fileContent );
        return fileMessage;
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
}
