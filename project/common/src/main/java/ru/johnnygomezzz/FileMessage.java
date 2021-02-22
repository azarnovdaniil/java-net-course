package ru.johnnygomezzz;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessage extends AbstractMessage {

    private String fileName;

    private final byte[] data;
    private int partNumber;
    private int partsCount;

    public FileMessage(String filename, int partNumber, int partsCount, byte[] data) {
        this.fileName = filename;
        this.partNumber = partNumber;
        this.partsCount = partsCount;
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }

    public FileMessage(Path path) throws IOException {
        fileName = path.getFileName().toString();
        data = Files.readAllBytes(path);
    }
}
