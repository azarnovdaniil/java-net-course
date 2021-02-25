package ru.daniilazarnov;

import ru.daniilazarnov.operationWithFile.TypeOperation;

import java.io.Serializable;

public class DataMsg implements Serializable {

    private final Command command;
    private final byte[] bytes;
    private TypeOperation type;

    public DataMsg(Command command, byte[] bytes) {
        this.command = command;
        this.bytes = bytes;
        this.type = null;
    }

    public DataMsg(Command command, byte[] bytes, TypeOperation type) {
        this(command, bytes);
        this.type = type;
    }

    public Command getCommand() {
        return command;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public TypeOperation getType() {
        return type;
    }

    public void setType(TypeOperation type) {
        this.type = type;
    }

    public static DataMsg createMsg(Command command, Object obj) {
        try {
            return new DataMsg(command, ConvertToByte.serialize(obj));
        } catch (Exception e) {
            return new DataMsg(Command.createError(""), null);
        }
    }

    public static DataMsg createMsg(Command command, Object obj, TypeOperation type) {
        try {
            return new DataMsg(command, ConvertToByte.serialize(obj), type);
        } catch (Exception e) {
            return new DataMsg(Command.createError(""), null, type);
        }
    }
}
