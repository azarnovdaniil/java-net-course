package ru.sviridovaleksey.commands;

import java.io.Serializable;

public class ShowAllInDirectory implements Serializable {

    private final String userName;
    private final String message;

    public ShowAllInDirectory(String userName, String message) {
        this.userName = userName;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getUserName() {
        return userName;
    }
}
