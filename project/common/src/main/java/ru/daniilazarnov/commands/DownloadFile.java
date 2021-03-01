package ru.daniilazarnov.commands;

import ru.daniilazarnov.MessagePacket;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class DownloadFile extends Commands {
    public DownloadFile(String string) {
    }

    @Override
    public MessagePacket runCommands(MessagePacket messagePacket) {


        System.out.println("Реализовать скачивание файла");

        String userDir=messagePacket.getUserDir();
        Path userPath = Paths.get(userDir);
        String homeDirectory=messagePacket.getHomeDirectory();
        Path homePath=Paths.get(homeDirectory);
        Path homePath1=userPath.resolve(homePath);
        String filename=messagePacket.getFileName();
        Path filenamePath=Path.of(filename);
        try {
            messagePacket.setContent(Files.readAllBytes(filenamePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new MessagePacket();
    }

    @Override
    public MessagePacket runClientCommands(MessagePacket messagePacket) {

        try {
            Files.write(Path.of(messagePacket.getFileName()),  messagePacket.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}

