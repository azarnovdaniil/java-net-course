package ru.daniilazarnov.commands;


import java.io.Serializable;

public class PublicMessageCommandData implements Serializable {

    private final String sender;
    private final String message;

    public PublicMessageCommandData(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}