package ru.daniilazarnov.old;

import java.io.Serializable;

public class CommandMessageOld implements Serializable {
    String[] command;

    public CommandMessageOld(String[] command) {
        this.command = command;
    }


}
