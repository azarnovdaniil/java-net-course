package ru.daniilazarnov.database;

public interface DBCommands {
    boolean findUser(String login, String password);

    int addUser(String login, String password);
}
