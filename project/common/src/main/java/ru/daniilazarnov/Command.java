package ru.daniilazarnov;

import java.io.Serializable;

public class Command implements Serializable {
    private String command;
    private Object obj;

    public Command(String command) {
        this.command = command;
    }

    public Command(String command, Object obj) {
        this.command = command;
        this.obj = obj;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
