package ru.sviridovaleksey.commands;

import java.io.Serializable;

public class CreateNewDirectory implements Serializable {

    private final String userName;
    private final String directoryName;

    public CreateNewDirectory(String userName, String fileName) {
        this.userName = userName;
        this.directoryName = fileName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public String getUserName() {
        return userName;
    }
}
