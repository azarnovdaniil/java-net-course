package ru.daniilazarnov;

public enum Command {
    EXIT("exit"),
    UPLOAD("upload");

    private final String cmd;

    Command(String cmd) {
        this.cmd = cmd;
    }

    public String getCmd() {
        return cmd;
    }
}
