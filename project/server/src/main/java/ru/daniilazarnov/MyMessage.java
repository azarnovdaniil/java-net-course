package ru.daniilazarnov;

import java.io.Serializable;

public class MyMessage implements Serializable {

    private static final long serialVersionUID = 5193392663743561680L;

    private final String message;

    public MyMessage(String text) {

        this.message = text;
    }

    public String getText() {

        return message;
    }
}
