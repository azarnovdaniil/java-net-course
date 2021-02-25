package ru.daniilazarnov.commands;

import java.io.Serializable;

public class SendFileData implements Serializable {

    private final String username;
    private final String filename;
    private byte[] arr = null;

    public SendFileData(String username, String filename, byte[] arr) {
        this.username = username;
        this.filename = filename;
        this.arr = arr;
    }

    public String getUsername() {
        return username;
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getBytes() {
        return arr;
    }
}
