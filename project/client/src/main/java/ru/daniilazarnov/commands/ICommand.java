package ru.daniilazarnov.commands;

import ru.daniilazarnov.ClientConnection;

import java.io.IOException;

public interface ICommand {
    boolean apply(ClientConnection connection) throws IOException;
}
