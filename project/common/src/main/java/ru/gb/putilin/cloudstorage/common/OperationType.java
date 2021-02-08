package ru.gb.putilin.cloudstorage.common;

public enum OperationType {
    UPLOAD((byte) 1),
    DOWNLOAD((byte) 2),
    SHOW((byte) 3);

    private byte code;

    OperationType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
