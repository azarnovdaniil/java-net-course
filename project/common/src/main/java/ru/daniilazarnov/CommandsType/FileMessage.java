package ru.daniilazarnov.CommandsType;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessage implements Serializable {

    private String fileName;
    private String login;
    private byte[] data;
    private boolean isDirectory;
    private boolean isEmpty;

    public FileMessage(Path path) throws IOException {
        fileName = path.getFileName().toString();
        data = Files.readAllBytes(path);
        this.isDirectory = false;
        this.isEmpty = false;
    }
    public FileMessage(String fileName, boolean isDirectory, boolean isEmpty){
        this.fileName = fileName;
        this.isDirectory = isDirectory;
        this.isEmpty = isEmpty;
    }
    public FileMessage(String login, Path path) throws IOException, AccessDeniedException {
        fileName = path.getFileName().toString();
        data = Files.readAllBytes(path);
        this.login = login;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public String getLogin() {
        return login;
    }


    public byte[] getContent() {
        return new byte[0];
    }
}
