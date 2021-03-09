package ru.daniilazarnov;

import ru.daniilazarnov.MessagePacket;
import ru.daniilazarnov.commands.Commands;

import java.io.InputStream;
import java.util.Scanner;

public class ShowHelp extends Commands {
    @Override
    public MessagePacket runCommands(MessagePacket messagePacket) {
        return null;
    }

    @Override
    public MessagePacket runClientCommands(MessagePacket messagePacket) {
        return null;
    }

    @Override
    public MessagePacket runOutClientCommands(Scanner scanner, MessagePacket messagePacket) {
        PrintMessages hello = new PrintMessages();
        String fileName = "Help.txt";
        InputStream is = hello.getFileFromResourceAsStream(fileName);
        hello.printInputStream(is);
        messagePacket.setSenDToServer(false);
        return messagePacket;
    }
}
