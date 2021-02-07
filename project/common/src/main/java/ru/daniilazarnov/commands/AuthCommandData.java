package ru.daniilazarnov.commands;

import java.io.Serializable;

public class AuthCommandData implements Serializable {

    private final String login;
    private final String password;

    public AuthCommandData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
