package ru.atoroschin.auth;

import ru.atoroschin.Credentials;

public class User {
    private final String folder;
    private final String login;
    private final String password;
    private final int maxVolume;

    public User(String login, String password, String folder, int maxVolume) {
        this.folder = folder;
        this.login = login;
        this.password = password;
        this.maxVolume = maxVolume;
    }

    public String getLogin() {
        return login;
    }

    public String getFolder() {
        return folder;
    }

    public int getMaxVolume() {
        return maxVolume;
    }

    public boolean isAuth(Credentials credentials) {
        return login.equals(credentials.getLogin()) && password.equals(credentials.getPassword());
    }
}
