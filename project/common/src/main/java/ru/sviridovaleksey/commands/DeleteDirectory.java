package ru.sviridovaleksey.commands;

import java.io.Serializable;

public class DeleteDirectory implements Serializable {

    private final String userName;
    private final String directoryNumber;

    public DeleteDirectory(String userName, String directoryNumber) {
        this.userName = userName;
        this.directoryNumber = directoryNumber;
    }

    public String getDirectoryName() {
        return directoryNumber;
    }

    public String getUserName() {
        return userName;
    }
}
