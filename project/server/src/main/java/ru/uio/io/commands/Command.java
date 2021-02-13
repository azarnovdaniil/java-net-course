package ru.uio.io.commands;

import ru.uio.io.ClientHandler;

public abstract class Command {
    ClientHandler clientHandler;

    Command(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public abstract boolean execute();
}
