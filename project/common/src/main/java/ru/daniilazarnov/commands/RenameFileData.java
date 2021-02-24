package ru.daniilazarnov.commands;

import java.io.Serializable;

public class RenameFileData implements Serializable {

    private final String username;
    private final String filename;
    private final String filenameNew;

    public RenameFileData(String username, String filename1, String filename2) {
        this.username = username;
        this.filename = filename1;
        this.filenameNew = filename2;
    }

    public String getUsername() {
        return username;
    }

    public String getFilenameNew() {
        return filenameNew;
    }

    public String getFilename() {
        return filename;
    }
}
