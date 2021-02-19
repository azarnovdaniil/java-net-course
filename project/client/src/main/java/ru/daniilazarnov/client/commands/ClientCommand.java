package ru.daniilazarnov.client.commands;

public enum ClientCommand {
    UPLOAD("upload"),
    DOWNLOAD("download"),
    SHOW("show"),
    LOGIN("login");

    private String title;

    ClientCommand(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
