package ru.daniilazarnov.clientConnection.commands;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface ICommand {
    boolean apply(SocketChannel socketChannel) throws IOException;
}
