package ru.daniilazarnov;

import java.io.Serializable;

public enum Command implements Serializable {
    LIST("/list", " - get a list of all available files and directories"),
    DOWNLOAD("/download", " - download file from server"),
    UPLOAD("/upload", " - send file to server"),
    REMOVE("/remove", " - delete file on server"),
    MOVE("/move", " - moves the file to another directory"),
    CREATEDIR("/crdir", " - create directory on server"),
    EXIT("/exit", " - disconnect from server");

    private final String command;
    private final String description;
    private Object[] arg;

    Command(String command, String description, Object... arg) {
        this.command = command;
        this.description = description;
        this.arg = arg;
    }


    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public Object[] getArg() {
        return arg;
    }

    public void setArg(Object[] arg) {
        this.arg = arg;
    }
}
