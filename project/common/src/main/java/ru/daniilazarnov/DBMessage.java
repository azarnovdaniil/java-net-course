package ru.daniilazarnov;

public class DBMessage extends AbstractMessage{

    private String cmd;
    private String login;
    private String password;

    public DBMessage(String cmd, String login) {
        this.cmd = cmd;
        this.login = login;
    }
    public DBMessage(String cmd, String login, String password) {
        this.cmd = cmd;
        this.login = login;
        this.password = password;
    }

    public String getCmd() {
        return cmd;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
