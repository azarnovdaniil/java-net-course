package ru.daniilazarnov;

public class FileContainer {
    private byte[] filePart;
    private String name;

    FileContainer(byte[]filePart, String name){
        this.name=name;
        this.filePart=filePart;
    }

    public byte[] getFilePart() {
        return filePart;
    }

    public String getName() {
        return name;
    }
}
