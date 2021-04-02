package ru.daniilazarnov;

public class FileContainer {
    private final byte[] filePart;
    private final String name;

    FileContainer(byte[] filePart, String name) {
        this.name = name;
        this.filePart = filePart;
    }

    public byte[] getFilePart() {
        return filePart;
    }

    public String getName() {
        return name;
    }
}
