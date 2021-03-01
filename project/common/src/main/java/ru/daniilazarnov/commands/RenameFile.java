package ru.daniilazarnov.commands;

import ru.daniilazarnov.MessagePacket;

import java.nio.file.Path;

public final class RenameFile extends Commands {

    public RenameFile(String s) {
        this.messageForInput = s;
    }

    @Override
    public MessagePacket runCommands(MessagePacket messagePacket) {
        return new MessagePacket();
    }

    @Override
    public MessagePacket runClientCommands(MessagePacket messagePacket) {
        return null;
    }
}
