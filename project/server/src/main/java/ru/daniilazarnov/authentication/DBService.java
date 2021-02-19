package ru.daniilazarnov.authentication;

public interface DBService {
    boolean findUser(String login, String password);

    void addUser(String login, String password);
}
