package ru.daniilazarnov;

public enum CommandList {
    QUIT("q"),
    DOWNLOAD("d"),
    UPLOAD("u");

    private final String command;

    CommandList(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
