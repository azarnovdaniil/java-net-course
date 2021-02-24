package ru.daniilazarnov;

public enum CommandType {
    AUTH,
    AUTH_OK,
    AUTH_ERROR,
    INFO_MESSAGE,
    ERROR,
    END,
    SEND_FILE,
    RENAME_FILE,
    REMOVE_FILE,
    REQUEST_FILE,
    GET_DIR
}
