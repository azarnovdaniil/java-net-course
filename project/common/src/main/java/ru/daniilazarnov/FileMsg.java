package ru.daniilazarnov;

import java.io.Serializable;

public class FileMsg implements Serializable {
    private final String nameFile;
    private final byte[] file;

    public FileMsg(String nameFile, byte[] file) {
        this.nameFile = nameFile;
        this.file = file;
    }

    public String getNameFile() {
        return nameFile;
    }

    public byte[] getFile() {
        return file;
    }


}
