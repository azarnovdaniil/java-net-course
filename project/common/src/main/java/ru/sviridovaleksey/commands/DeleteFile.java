package ru.sviridovaleksey.commands;

import java.io.Serializable;

public class DeleteFile implements Serializable {

    private final String userName;
    private final String fileNumber;

    public DeleteFile(String userName, String fileName) {
        this.userName = userName;
        this.fileNumber = fileName;
    }

    public String getFileName() {
        return fileNumber;
    }

    public String getUserName() {
        return userName;
    }
}
