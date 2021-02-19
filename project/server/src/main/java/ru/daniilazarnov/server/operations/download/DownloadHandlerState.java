package ru.daniilazarnov.server.operations.download;

public enum DownloadHandlerState {
    PATH_LENGTH,
    PATH,
    SEND_FILE,
    COMPLETE;
}
