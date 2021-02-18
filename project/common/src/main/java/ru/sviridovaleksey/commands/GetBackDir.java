package ru.sviridovaleksey.commands;

import java.io.Serializable;

public class GetBackDir implements Serializable {

    private final String userName;
    private final String message;

    public GetBackDir(String userName, String message) {
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
