package ru.daniilazarnov.commands;

import java.io.Serializable;

public class PrivateMessageCommandData implements Serializable {

    private final String receiver;
    private final String message;

    public PrivateMessageCommandData(String receiver, String message) {
        this.receiver = receiver;
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }
}