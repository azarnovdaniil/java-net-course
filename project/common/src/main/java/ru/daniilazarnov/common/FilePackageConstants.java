package ru.daniilazarnov.common;

public enum FilePackageConstants {
    NAME_LENGTH_BYTES(4),
    FILE_LENGTH_BYTES(8);

    private int bytes;

    FilePackageConstants(int bytes) {
        this.bytes = bytes;
    }

    public int getCode() {
        return bytes;
    }

}
