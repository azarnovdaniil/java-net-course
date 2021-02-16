package ru.daniilazarnov.common.files;

public enum ReadState {
    NAME_LENGTH,
    NAME,
    FILE_LENGTH,
    FILE,
    COMPLETE;
}
