package ru.daniilazarnov;

public enum State {
    GET_COMMAND,
    SEND_FILE,
    NAME,
    NAME_LENGTH,
    FILE_LENGTH,
    GET_FILE,
    SEND_LIST,
    DELETE_FILE,
    START_TYPE,
    CREATE_DIR,
    MOVE_FORWARD,
    MOVE_BACK,
}
