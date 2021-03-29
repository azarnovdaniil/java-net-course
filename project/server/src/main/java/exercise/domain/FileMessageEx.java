package exercise.domain;

import java.io.Serializable;

public class FileMessageEx implements Serializable {

    private final String fileName;
    private final byte[] content;

    public FileMessageEx(String fileName, byte[] content) {
        this.fileName = fileName;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getContent() {
        return content;
    }
}
