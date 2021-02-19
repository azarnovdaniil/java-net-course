package ru.daniilazarnov;

public class RequestMessage extends AbstractMessage {

    private Commands command;
    private String filename;
    private String newFileName;
    private String login;

    public RequestMessage(Commands command, String login) {
        this.command = command;
        this.login = login;
    }

    public RequestMessage(Commands command, String filename, String login) {
        this.filename = filename;
        this.command = command;
        this.login = login;
    }

    public RequestMessage(Commands command, String filename, String newFileName, String login) {
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

    public Commands getCommand() {
        return command;
    }
}
