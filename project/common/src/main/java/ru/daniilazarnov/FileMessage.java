package ru.daniilazarnov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessage extends AbstractMessage {

    private String fileName;
    private String account;
    private  byte[] data;

    public FileMessage() {

    }

    public String getAccount() {
        return account;
    }

    public FileMessage(String account) {
        this.account = account;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }

    public FileMessage(Path path) throws IOException {
        account = path.getName(6).toString();
        fileName = path.getFileName().toString();
        data = Files.readAllBytes(path);

    }
}
