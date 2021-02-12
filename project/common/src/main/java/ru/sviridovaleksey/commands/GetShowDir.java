package ru.sviridovaleksey.commands;

import java.io.Serializable;

public class GetShowDir implements Serializable {

    private final String userName;
    private final String message;

    public GetShowDir (String userName, String message) {
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