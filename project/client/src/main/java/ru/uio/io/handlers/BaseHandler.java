package ru.uio.io.handlers;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public abstract class BaseHandler {
    protected final DataInputStream in;
    protected final DataOutputStream out;
    protected String command;

    public BaseHandler(DataInputStream in, DataOutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    abstract public void workWithMessage();
}
