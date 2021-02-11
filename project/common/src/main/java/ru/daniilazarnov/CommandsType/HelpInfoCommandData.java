package ru.daniilazarnov.CommandsType;

import java.io.Serializable;

public class HelpInfoCommandData implements Serializable {

    private final String message;
    private final String sender;

    public HelpInfoCommandData(String message, String sender) {
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
