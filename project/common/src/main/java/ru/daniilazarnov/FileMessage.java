package ru.daniilazarnov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessage extends AbstractMessage {

    private String filename;
    private byte[] data;

    public FileMessage(Path path) {
        filename = path.getFileName().toString();
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        }
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }

}
