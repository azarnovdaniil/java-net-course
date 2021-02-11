package ru.daniilazarnov;

public class FileMessage extends AbstractMessage {

    private String filename;
    private byte[] data;
    private int partNumber;
    private int partsCount;
    private String login;

    public FileMessage(String filename, int partNumber, int partsCount, byte[] data, String login) {
        this.filename = filename;
        this.partNumber = partNumber;
        this.partsCount = partsCount;
        this.data = data;
        this.login = login;
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }

    public String getLogin() {
        return login;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public int getPartsCount() {
        return partsCount;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }
}
