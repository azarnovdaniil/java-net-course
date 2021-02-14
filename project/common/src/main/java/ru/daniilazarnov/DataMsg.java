package ru.daniilazarnov;

import java.io.Serializable;

public class DataMsg implements Serializable {

    private final Command command;
    private final byte[] bytes;
    private final String[] paths;

    public DataMsg(Command command, byte[] bytes) {
        this.command = command;
        this.bytes = bytes;
        this.paths = null;
    }

//    public DataMsg(Command command, byte[] bytes, String[] paths) {
//        this.command = command;
//        this.bytes = bytes;
//        this.paths = paths;
//    }

    public Command getCommand() {
        return command;
    }

    public byte[] getBytes() {
        return bytes;
    }

//    public String[] getPaths() {
//        return paths;
//    }
}
