package ru.daniilazarnov.command;

import java.io.Serializable;

public class BaseCommand implements Serializable {
    private String name;

    public BaseCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}