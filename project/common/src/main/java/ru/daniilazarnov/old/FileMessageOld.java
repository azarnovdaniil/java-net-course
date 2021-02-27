package ru.daniilazarnov.old;

import java.io.Serializable;

public class FileMessageOld implements Serializable {
    private final String fileName;
    private final byte[] content;

    public FileMessageOld(String fileName, byte[] content) {
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
