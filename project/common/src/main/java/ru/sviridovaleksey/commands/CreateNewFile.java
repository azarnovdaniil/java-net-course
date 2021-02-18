package ru.sviridovaleksey.commands;

import java.io.Serializable;

public class CreateNewFile implements Serializable {

    private final String userName;
    private final String fileName;

    public CreateNewFile(String userName, String fileName) {
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
