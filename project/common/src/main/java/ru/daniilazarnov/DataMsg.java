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

    protected static DataMsg createMsg(Command command, Object obj) {
        try {
            return new DataMsg(command, ConvertToByte.serialize(obj));
        } catch (Exception e) {
            return new DataMsg(Command.createError(""), null);
        }
    }
}
