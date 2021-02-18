package ru.sviridovaleksey.commands;

import java.io.Serializable;

public class PasteFile implements Serializable {

    private final String userName;


    public PasteFile(String userName) {
        this.userName = userName;

    }


    public String getUserName() {
        return userName;
    }
}
