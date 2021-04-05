package ru.daniilazarnov.clientConnection.commands;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class UnknownCommand implements ICommand {

    @Override
    public boolean apply(SocketChannel socketChannel) throws IOException {
        System.out.println("Wrong command");
        return false;
    }
}
