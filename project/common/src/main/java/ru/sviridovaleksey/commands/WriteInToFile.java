package ru.sviridovaleksey.commands;

import java.io.Serializable;

public class WriteInToFile implements Serializable {

    private final String userName;
    private final String fileName;
    private final byte[] data;
    private final long cell;
    private final boolean endWrite;

    public WriteInToFile(String userName, String fileName, byte[] data, long cell, boolean endWrite) {
        this.userName = userName;
        this.fileName = fileName;
        this.data = data;
        this.cell = cell;
        this.endWrite = endWrite;
    }

    public byte[] getData() {
        return data;
    }

    public String getUserName() {
        return userName;
    }

    public String getFileName() {
        return fileName; }

    public long getCell() {
        return cell;
    }

    public Boolean getEndWrite() {
        return  endWrite; }
}
