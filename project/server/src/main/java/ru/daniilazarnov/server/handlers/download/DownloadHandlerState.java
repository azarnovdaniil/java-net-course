package ru.daniilazarnov.server.handlers.download;

public enum DownloadHandlerState {
    PATH_LENGTH,
    PATH,
    SEND_FILE,
    COMPLETE;
}
