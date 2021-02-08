package ru.daniilazarnov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessage extends AbstractMessage {

    private String filename;
    private byte[] data;
    private String login;

    public FileMessage(Path path, String login) {
        filename = path.getFileName().toString();
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        }
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
}
