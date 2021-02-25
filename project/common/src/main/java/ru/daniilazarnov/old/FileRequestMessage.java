package ru.daniilazarnov.old;

import java.io.Serializable;

public class FileRequestMessage implements Serializable {
    String fileName;

    public FileRequestMessage(String fileName) {
        this.fileName = fileName;
    }

    public String getRequest() {
        return fileName;
    }
}
