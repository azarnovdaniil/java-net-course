package ru.johnnygomezzz.commands;

public enum Commands {
    CREATE("/create", "Создать файл"),
    QUIT("/quit", "Выход");

    Commands() {

    }

    private String stringValue;
    private String helpValue;

    Commands(String stringValue, String helpValue) {
        this.stringValue = stringValue;
        this.helpValue = helpValue;
    }
}

