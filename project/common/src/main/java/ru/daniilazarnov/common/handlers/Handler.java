package ru.daniilazarnov.common.handlers;

public interface Handler {
    void handle() throws HandlerException;
    boolean isComplete();
}
