package ru.atoroschin.auth;

public class User {
    private final String folder;
    private final String login;
    private final String password;
    private final int ID;
    private final int maxVolume;

    public User(int id, String login, String password, String folder, int maxVolume) {
        this.folder = folder;
        this.login = login;
        this.password = password;
        this.maxVolume = maxVolume;
        ID = id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFolder() {
        return folder;
    }

    public int getID() {
        return ID;
    }

    public int getMaxVolume() {
        return maxVolume;
    }
}
