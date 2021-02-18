package ru.daniilazarnov;


import java.io.Serializable;


public class MyMessage extends AbstractMessage implements Serializable {
    private final String myMessage;

    public String getMyMessage() {
        return myMessage;
    }

    public MyMessage(String myMessage) {
        this.myMessage = myMessage;
    }



}
