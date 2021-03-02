package ru.daniilazarnov;

public class FileRequest extends AbstractMessage {
    private final String filename;
    private final String account;
//    public FileRequest() {
//
//    }

    public String getAccount() {
        return account;
    }

    public String getFilename() {
        return filename;
    }

    public FileRequest(String filename, String account) {
        this.account = account;
        this.filename = filename;
    }


}
