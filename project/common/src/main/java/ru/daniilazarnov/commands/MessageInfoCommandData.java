package ru.daniilazarnov.commands;

import java.io.Serializable;

public class MessageInfoCommandData implements Serializable {

    private final String message;
    private final String sender;

    public MessageInfoCommandData(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}
