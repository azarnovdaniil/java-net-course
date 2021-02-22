package ru.daniilazarnov;

public class FileRequest extends AbstractMessage {
    private  String filename;

    public FileRequest() {

    }

    public String getFilename() {
        return filename;
    }

    public FileRequest(String filename) {
        this.filename = filename;
    }

}
