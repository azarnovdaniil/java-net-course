package ru.daniilazarnov.commands;

import ru.daniilazarnov.ClientConnection;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface ICommands {
    boolean apply(ClientConnection connection) throws IOException;
}
