package ru.daniilazarnov.commands;

import java.io.Serializable;

public class GetDirCommandData implements Serializable {
    private final String username;

    public GetDirCommandData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getUserDir() {
        return "DIR";
    }
}
