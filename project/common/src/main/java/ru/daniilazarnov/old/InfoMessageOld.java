package ru.daniilazarnov.old;

import java.io.Serializable;
import java.util.List;

public class InfoMessageOld implements Serializable {
    String message;
    List<String> listOfFiles;

    public InfoMessageOld(String message) {
        this.message = message;
    }

    public InfoMessageOld(List<String> listOfFiles) {
        this.listOfFiles = listOfFiles;
    }

    public List<String> getListOfFiles() {
        return listOfFiles;
    }

    public String getMessage() {
        return message;
    }
}
