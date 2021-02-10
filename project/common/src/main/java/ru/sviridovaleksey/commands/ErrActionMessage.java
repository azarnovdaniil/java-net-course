package ru.sviridovaleksey.commands;

import java.io.Serializable;

public class ErrActionMessage implements Serializable {

    private final String userName;
    private final String message;

    public ErrActionMessage(String message, String userName) {
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