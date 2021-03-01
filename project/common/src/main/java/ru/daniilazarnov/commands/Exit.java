package ru.daniilazarnov.commands;

import ru.daniilazarnov.MessagePacket;

public class Exit extends Commands {
    @Override
    public MessagePacket runCommands(MessagePacket messagePacket) {
        return messagePacket;
    }

    @Override
    public MessagePacket runClientCommands(MessagePacket messagePacket) {
        return null;
    }
}
