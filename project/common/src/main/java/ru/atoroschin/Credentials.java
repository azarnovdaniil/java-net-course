package ru.atoroschin;

import java.util.List;
import java.util.StringJoiner;

public class Credentials {
    private final String login;
    private final String password;
    private final boolean enable;

    public Credentials() {
        this(List.of(""));
    }

    public Credentials(String login, String password) {
        this(List.of(login, password));
    }

    public Credentials(List<String> list) {
        if (list.size()==2) {
            login = list.get(0);
            password = list.get(1);
            enable = true;
        } else {
            login = "";
            password = "";
            enable = false;
        }
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

    public boolean isEnable() {
        return enable;
    }
}
