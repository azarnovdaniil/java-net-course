package ru.daniilazarnov.commands;

import java.io.Serializable;

public class RequestFileData implements Serializable {

    private final String username;
    private final String filename;

    public RequestFileData(String username, String filename) {
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
