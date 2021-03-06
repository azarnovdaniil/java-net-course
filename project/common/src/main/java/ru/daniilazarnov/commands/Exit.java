package ru.daniilazarnov.commands;

import ru.daniilazarnov.MessagePacket;

import java.util.Scanner;

public class Exit extends Commands {
    @Override
    public MessagePacket runCommands(MessagePacket messagePacket) {
        return messagePacket;
    }

    @Override
    public MessagePacket runClientCommands(MessagePacket messagePacket) {
        return null;
    }
    @Override
    public MessagePacket runOutClientCommands(Scanner scanner, MessagePacket messagePacket) {
        return messagePacket;
    }
}
