package ru.daniilazarnov.common.commands;

public enum Commands {
    UPLOAD("upload", (byte) 1),
    DOWNLOAD("download", (byte) 2),
    SHOW("show", (byte) 3),
    LOGIN("login", (byte) 4),
    MESSAGE("message", (byte) 5),
    EXIT("exit", (byte) 6),;

    private String title;
    private byte code;

    Commands(String title, byte code) {
        this.title = title;
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public byte getCode() {
        return code;
    }
}
