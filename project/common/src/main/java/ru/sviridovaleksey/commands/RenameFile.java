package ru.sviridovaleksey.commands;

import java.io.Serializable;

public class RenameFile implements Serializable {

    private final String userName;
    private final String oldName;
    private final String newName;

    public RenameFile(String userName, String oldName, String newName) {
        this.userName = userName;
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getOldName() {
        return oldName;
    }

    public String getNewName() {
        return newName;
    }

    public String getUserName() {
        return userName;
    }
}
