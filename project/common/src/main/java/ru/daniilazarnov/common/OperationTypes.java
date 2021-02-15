package ru.daniilazarnov.common;

public enum OperationTypes {
    UPLOAD((byte) 1),
    DOWNLOAD((byte) 2),
    SHOW((byte) 3);

    private byte code;

    OperationTypes(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
