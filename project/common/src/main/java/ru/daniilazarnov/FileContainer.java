package ru.daniilazarnov;

public class FileContainer {
    private final byte[] filePart;
    private final String name;

    /**
     * Utility class, used to send files.
     * @param filePart - file chunk.
     * @param name - name of the file.
     */

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
