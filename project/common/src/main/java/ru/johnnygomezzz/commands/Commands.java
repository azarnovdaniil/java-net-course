package ru.johnnygomezzz.commands;

public enum Commands {
    HELP("/help", "подсказка"),
    TOUCH("/touch", "создать файл - /touch [имя файла] [содержимое]"),
    LS("/ls", "просмотреть содержимое каталога - /ls [имя каталога], /ls / для просмотра текущего"),
    DELETE("/delete", "удалить файл - /delete [имя файла]"),
    DOWNLOAD("/download", "скачать файл с сервера - /download [имя файла]"),
    UPLOAD("/upload", "отправить файл на сервер - /upload [имя файла]"),
    MKDIR("/mkdir", "создать каталог - /mkdir [имя каталога]"),
    QUIT("/quit", "выход");

    private String name;
    private String helpValue;

    Commands(String name, String helpValue) {
        this.name = name;
        this.helpValue = helpValue;
    }

    public String getName() {
        return name;
    }

    public String getHelpValue() {
        return helpValue;
    }
}

