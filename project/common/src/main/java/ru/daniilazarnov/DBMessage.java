package ru.daniilazarnov;

public class DBMessage extends AbstractMessage {

    private Commands command;
    private String login;
    private String password;

    public DBMessage(String login) {
        this.login = login;
    }

    public DBMessage(Commands command, String login, String password) {
        this.command = command;
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Commands getCommand() {
        return command;
    }
}
