package ru.daniilazarnov.commands;

import ru.daniilazarnov.ClientConnection;

import java.io.IOException;

public class UnknownCommand implements ICommand {

    @Override
    public boolean apply(ClientConnection connection) throws IOException {
        System.out.println("Wrong command");
        return false;
    }
}
