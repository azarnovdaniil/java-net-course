package ru.daniilazarnov;

public class RequestMsg extends AbstractMsg {

    private CommandList command;
    private String filename;
    private String newFileName;
    private String login;

    public RequestMsg(CommandList command, String login) {
        this.command = command;
        this.login = login;
    }

    public RequestMsg(CommandList command, String filename, String login) {
        this.filename = filename;
        this.command = command;
        this.login = login;
    }

    public RequestMsg(CommandList command, String filename, String newFileName, String login) {
        this.filename = filename;
        this.command = command;
        this.newFileName = newFileName;
        this.login = login;
    }

    public String getFilename() {
        return filename;
    }

    public String getNewFileName() {
        return newFileName;
    }

    public String getLogin() {
        return login;
    }

    public CommandList getCommand() {
        return command;
    }
}

