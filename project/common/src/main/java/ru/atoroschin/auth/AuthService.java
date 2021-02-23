package ru.atoroschin.auth;

import ru.atoroschin.Credentials;

public interface AuthService {
    boolean isAuth(Credentials credentials);

    String getUserFolder(String login) throws IllegalAccessException;

    int getMaxVolume(String login) throws IllegalAccessException;
}
