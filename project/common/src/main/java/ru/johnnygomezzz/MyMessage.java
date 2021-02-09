package ru.johnnygomezzz;

import java.io.Serializable;

public class MyMessage implements Serializable {

    private static final long serialVersionUID = 202102081559L;

    private final String text;

    public MyMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
