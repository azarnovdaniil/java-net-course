package ru.daniilazarnov.commands;

import ru.daniilazarnov.MessagePacket;

import java.io.Serializable;
import java.util.Scanner;


abstract public class Commands implements Serializable {
    String fileServerName;
    String newFileServerName;
    String messageForInput=null;

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

    public abstract MessagePacket runOutClientCommands(Scanner scanner, MessagePacket messagePacket);


}
