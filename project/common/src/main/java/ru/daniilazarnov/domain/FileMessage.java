package ru.daniilazarnov.domain;

import java.io.Serializable;

public class FileMessage implements Serializable {

    private final String fileName;
    private final Integer sizeFile;
    private final byte[] content;


    public FileMessage (String fileName, Integer sizeFile, byte[] content) {
        this.fileName = fileName;
        this.sizeFile = sizeFile;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public Integer getSizeFile () {
        return sizeFile;
    }
}
