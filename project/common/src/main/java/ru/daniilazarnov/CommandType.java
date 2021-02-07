package ru.daniilazarnov;

public enum CommandType {
    AUTH,
    AUTH_OK,
    AUTH_ERROR,
    PRIVATE_MESSAGE,
    PUBLIC_MESSAGE,
    INFO_MESSAGE,
    ERROR,
    END,
    UPDATE_USERS_LIST,
    SEND_FILE,
    REQUEST_FILE,
    GET_DIR
}
