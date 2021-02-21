package ru.daniilazarnov.newp;

import java.io.Serializable;

public class CommandMessage implements Serializable {
    String[] command;

    public CommandMessage(String[] command) {
        this.command = command;
    }


}
