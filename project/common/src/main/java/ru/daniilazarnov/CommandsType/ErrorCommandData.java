package ru.daniilazarnov.CommandsType;

import java.io.Serializable;

public class ErrorCommandData implements Serializable {

    private final String errorMessage;

    public ErrorCommandData(String errorMessage) {

        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {

        return errorMessage;
    }
}
