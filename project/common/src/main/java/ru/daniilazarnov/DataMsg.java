package ru.daniilazarnov;

import java.io.Serializable;

public class DataMsg implements Serializable {

    private final Command command;
    private final byte[] bytes;

    public DataMsg(Command command, byte[] bytes) {
        this.command = command;
        this.bytes = bytes;
    }

    public Command getCommand() {
        return command;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
