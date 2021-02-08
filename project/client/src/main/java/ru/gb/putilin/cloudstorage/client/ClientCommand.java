package ru.gb.putilin.cloudstorage.client;

public enum ClientCommand {
    UPLOAD("upload"),
    DOWNLOAD("download"),
    SHOW("show");

    private String title;

    ClientCommand(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
