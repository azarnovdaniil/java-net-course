package ru.daniilazarnov.server.operations.login;

public enum LoginHandlerState {
    NAME_LENGTH,
    NAME,
    PASS_LENGTH,
    PASS,
    COMPLETE;
}
