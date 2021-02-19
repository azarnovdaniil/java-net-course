package ru.daniilazarnov.server.database;

public interface Authentication
{
    boolean login(String name, String password) throws DatabaseException;
}
