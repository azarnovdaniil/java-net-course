package ru.daniilazarnov;


import ru.daniilazarnov.commands.Commands;
import java.util.Scanner;

public class ShowHello extends Commands {
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
        PrintMessages print = new PrintMessages();
        print.printFile("MessageHello.txt");
        messagePacket.setSenDToServer(false);
        return messagePacket;
    }
}
