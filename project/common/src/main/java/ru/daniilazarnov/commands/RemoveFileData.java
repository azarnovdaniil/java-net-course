package ru.daniilazarnov.commands;

import java.io.Serializable;

public class RemoveFileData implements Serializable {
    private final String username;
    private final String filename;

    public RemoveFileData(String username, String filename) {
        this.username = username;
        this.filename = filename;
    }

    public String getUsername() {
        return username;
    }

    public String getFilename() {
        return filename;
    }
}
