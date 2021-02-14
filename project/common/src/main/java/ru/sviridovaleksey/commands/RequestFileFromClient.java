package ru.sviridovaleksey.commands;

import java.io.Serializable;

public class RequestFileFromClient implements Serializable {

    private final String userName;
    private final String fileName;

    public RequestFileFromClient (String userName, String fileName) {
        this.userName = userName;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUserName() {
        return userName;
    }
}