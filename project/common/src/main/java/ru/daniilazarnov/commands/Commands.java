package ru.daniilazarnov.commands;

import ru.daniilazarnov.MessagePacket;

import java.io.Serializable;
import java.nio.file.Path;


abstract public class Commands implements Serializable {
    String fileServerName;
    String newFileServerName;
    String messageForInput;

    public String getMessageForInput() {
        return messageForInput;
    }

    public Commands() {
    }

    public Commands(String s) {
        this.messageForInput = s;
    }

    public void setMessageForInput(String messageForInput) {
        this.messageForInput = messageForInput;
    }


    public abstract MessagePacket runCommands(MessagePacket messagePacket);

    public abstract MessagePacket runClientCommands(MessagePacket messagePacket);

}
