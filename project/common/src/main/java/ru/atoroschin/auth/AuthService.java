package ru.atoroschin.auth;

public interface AuthService {
    boolean auth(String login, String pass);

    String getUserFolder(String login);

    int getMaxVolume(String login);
}
