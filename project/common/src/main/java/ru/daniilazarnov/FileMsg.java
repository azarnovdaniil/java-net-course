package ru.daniilazarnov;

import java.io.Serializable;

public class FileMsg implements Serializable {
    private String nameFile;
    private final byte[] bytes;

    public FileMsg(String nameFile, byte[] bytes) {
        this.nameFile = nameFile;
        this.bytes = bytes;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getNameFile() {
        return nameFile;
    }

    public byte[] getBytes() {
        return bytes;
    }


}
