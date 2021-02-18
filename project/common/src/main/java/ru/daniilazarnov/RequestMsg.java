package ru.daniilazarnov;

public class RequestMsg extends AbstractMsg {

    private String cmd;
    private String filename;
    private String newFileName;
    private String login;

    public RequestMsg(String cmd, String login) {
        this.cmd = cmd;
        this.login = login;
    }

    public RequestMsg(String cmd, String filename, String login) {
        this.filename = filename;
        this.cmd = cmd;
        this.login = login;
    }

    public RequestMsg(String cmd, String filename, String newFileName, String login) {
        this.filename = filename;
        this.cmd = cmd;
        this.newFileName = newFileName;
        this.login = login;
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

    public String getLogin() {
        return login;
    }
}

