package ru.daniilazarnov.auth;


import ru.daniilazarnov.entity.User;

public interface AuthService {
    User doAuth(String login, String password);
    void addUser(User user);
    boolean doReg(String login, String password);
}
