package ru.atoroschin.auth;

public interface AuthService {
    boolean isAuth(String login, String pass);

    String getUserFolder(String login);

    int getMaxVolume(String login);
}
