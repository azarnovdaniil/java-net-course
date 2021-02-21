package ru.atoroschin;

import java.util.StringJoiner;

public class Credentials {
    private final String login;
    private final String password;

    public Credentials() {
        this("", "");
    }

    public Credentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getLine() {
        StringJoiner stringJoiner = new StringJoiner(" ","","");
        stringJoiner.add(login).add(password);
        return stringJoiner.toString();
    }
}
