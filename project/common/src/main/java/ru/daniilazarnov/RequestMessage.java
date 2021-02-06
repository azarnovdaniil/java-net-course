package ru.daniilazarnov;

public class RequestMessage extends AbstractMessage {

    private String cmd;
    private String filename;
    private String newFileName;

    public RequestMessage(String cmd) {
        this.cmd = cmd;
    }

    public RequestMessage(String cmd, String filename) {
        this.filename = filename;
        this.cmd = cmd;
    }

    public RequestMessage(String cmd, String filename, String newFileName) {
        this.filename = filename;
        this.cmd = cmd;
        this.newFileName = newFileName;
    }

    public String getFilename() {
        return filename;
    }

    public String getCmd() {
        return cmd;
    }

    public String getNewFileName() {
        return newFileName;
    }
}
