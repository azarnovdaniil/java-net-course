package ru.daniilazarnov;

public class DBMsg extends AbstractMsg {

    private String cmd;
    private String login;
    private String password;

    public DBMsg(String login) {
        this.login = login;
    }

    public DBMsg(String cmd, String login, String password) {
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