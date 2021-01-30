package ru.daniilazarnov;

import java.io.Serializable;
import java.nio.file.Path;

public class MyMessage implements Serializable {

    private static final long serialVersionUID = 5193392663743561680L;

    private Path text;

    public Path getText() {
        return text;
    }

    public MyMessage(Path text) {
        this.text = text;
    }
}

