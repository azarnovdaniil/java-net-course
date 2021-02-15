package ru.daniilazarnov;

import java.io.Serializable;

public class FileMessage implements Serializable {
    private String fileName;
    private byte[] content;

    public FileMessage(String fileName, byte[] content) {
        this.fileName = fileName;
        this.content = content;
    }

    String getFileName() {
        return fileName;
    }

    byte[] getContent() {
        return content;
    }
}
