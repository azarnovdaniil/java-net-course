package ru.daniilazarnov;

/**
 * содержит список возможных состояний сервера
 */
public enum State {
    IDLE,
    NAME_LENGTH,
    NAME,
    FILE_LENGTH,
    FILE
}
