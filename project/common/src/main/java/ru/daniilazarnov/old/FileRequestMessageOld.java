package ru.daniilazarnov.old;

import java.io.Serializable;

public class FileRequestMessageOld implements Serializable {
    String fileName;

    public FileRequestMessageOld(String fileName) {
        this.fileName = fileName;
    }

    public String getRequest() {
        return fileName;
    }
}
