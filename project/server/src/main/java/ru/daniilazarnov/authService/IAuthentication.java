package ru.daniilazarnov.authService;

public interface IAuthentication {
    boolean auth(String login, String password);
}
