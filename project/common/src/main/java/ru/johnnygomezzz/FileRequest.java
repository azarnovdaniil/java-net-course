package ru.johnnygomezzz;

public class FileRequest extends AbstractMessage {
    private final String filename;

    public String getFilename() {
        return filename;
    }

    public FileRequest(String filename) {
        this.filename = filename;
    }

}
