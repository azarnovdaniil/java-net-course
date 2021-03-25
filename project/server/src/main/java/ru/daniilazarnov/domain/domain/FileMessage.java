package ru.daniilazarnov.domain.domain;

import java.io.Serializable;

public class FileMessage implements Serializable {

    private final String fileName;
    private final byte[] content;

    public FileMessage(String fileName, byte[] content) {
        this.fileName = fileName;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getContent() {
        return content;
    }
}
