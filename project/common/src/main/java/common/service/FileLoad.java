package common.service;

import java.io.Serializable;

public class FileLoad implements Serializable {

    private String sourcePath;
    private String dstPath;
    private int startPos;

    private int countProgress = 0;
    private int countParts = 1;

    private byte[] bytes;
    private int byteRead;
    private boolean isNotLastPart = true;

    public FileLoad(String sourcePath, String dstPath) {
        this.sourcePath = sourcePath;
        this.dstPath = dstPath;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public String getDstPath() {
        return dstPath;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getCountProgress() {
        return countProgress;
    }

    public void setCountProgress(int countProgress) {
        this.countProgress = countProgress;
    }

    public int getCountParts() {
        return countParts;
    }

    public void setCountParts(int countParts) {
        this.countParts = countParts;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getByteRead() {
        return byteRead;
    }

    public void setByteRead(int byteRead) {
        this.byteRead = byteRead;
    }

    public boolean isNotLastPart() {
        return isNotLastPart;
    }

    public void setNotLastPart(boolean notLastPart) {
        isNotLastPart = notLastPart;
    }

}
