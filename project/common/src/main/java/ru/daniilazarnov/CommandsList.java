package ru.daniilazarnov;

public enum CommandsList {

    AUTH,
    AUTH_OK,
    AUTH_ERROR,
    HELP_MESSAGE, // Нужна другая реализация, вероятней всего при запуске клиента
    END,
    UPLOAD,
    DOWNLOAD,
    SHOW_FILES_LIST,
    RENAME_FILES,
    DELETE_FILES,

}
