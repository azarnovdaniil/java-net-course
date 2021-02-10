package ru.daniilazarnov;

public enum Command {
    LS("ls", (byte) 1),
    SEND("send",(byte) 2);

    byte signal;

    Command(String name,byte signal) {
        this.signal = signal;
    }
}
