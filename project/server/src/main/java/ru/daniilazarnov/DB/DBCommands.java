package ru.daniilazarnov.DB;


public interface DBCommands {
    boolean findUser(String login, String password);

    int addUser(String login, String password);
}