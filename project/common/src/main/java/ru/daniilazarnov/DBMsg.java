package ru.daniilazarnov;

public class DBMsg extends AbstractMsg {

    private CommandList command;
    private String login;
    private String password;

    public DBMsg(String login) {
        this.login = login;
    }

    public DBMsg(CommandList command, String login, String password) {
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

    public CommandList getCommand() {
        return command;
    }
}