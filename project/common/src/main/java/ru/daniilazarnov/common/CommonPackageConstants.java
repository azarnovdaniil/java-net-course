package ru.daniilazarnov.common;

public enum CommonPackageConstants {
    CONTENT_LENGTH_BYTES(4),
    FILE_LENGTH_BYTES(8);

    private int bytes;

    CommonPackageConstants(int bytes) {
        this.bytes = bytes;
    }

    public int getCode() {
        return bytes;
    }

}
