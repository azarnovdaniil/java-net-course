package ru.johnnygomezzz.commands;

public enum Commands {
    HELP("/help", "подсказка"),
    TOUCH("/touch", "создать файл - /touch [имя файла] [содержимое]"),
    STOUCH("/stouch", "создать файл на сервере - /stouch [имя файла] [содержимое]"),
    LS("/ls", "просмотреть содержимое каталога - /ls [имя каталога], /ls / для просмотра текущего"),
    SLS("/sls", "просмотреть содержимое каталога на сервере - /sls [имя каталога], "
            + "/sls / для просмотра текущего"),
    DELETE("/delete", "удалить файл - /delete [имя файла]"),
    SDELETE("/sdelete", "удалить файл на сервере - /sdelete [имя файла]"),
    DOWNLOAD("/download", "скачать файл с сервера - /download [имя файла]"),
    UPLOAD("/upload", "отправить файл на сервер - /upload [имя файла]"),
    MKDIR("/mkdir", "создать каталог - /mkdir [имя каталога]"),
    SMKDIR("/smkdir", "создать каталог на сервере - /smkdir [имя каталога]"),
    RENAME("/rename", "переименовать файл - /rename [имя файла] [новое имя файла]"),
    SRENAME("/srename", "переименовать файл на сервере - /srename [имя файла] [новое имя файла]"),
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

