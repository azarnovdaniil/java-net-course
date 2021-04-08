package ru.daniilazarnov.command;

import java.io.Serializable;

public class CommandRename implements Serializable {
    private String oldName;
    private String newName;

    public CommandRename(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getOldName() {
        return oldName;
    }

    public String getNewName() {
        return newName;
    }
}
