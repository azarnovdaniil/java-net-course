package ru.daniilazarnov.newp;

import java.io.Serializable;
import java.util.List;

public class InfoMessage implements Serializable {
    String message;
    List<String> listOfFiles;

    public InfoMessage(String message) {
        this.message = message;
    }

    public InfoMessage(List<String> listOfFiles) {
        this.listOfFiles = listOfFiles;
    }

    public List<String> getListOfFiles() {
        return listOfFiles;
    }

    public String getMessage() {
        return message;
    }
}
