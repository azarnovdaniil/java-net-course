package ru.daniilazarnov;

import java.io.Serializable;

public class File implements Serializable {

    private final String fileName;

    private final byte[] content;

    public File(String fileName, byte[] content) {
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
