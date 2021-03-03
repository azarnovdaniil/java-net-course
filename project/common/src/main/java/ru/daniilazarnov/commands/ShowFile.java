package ru.daniilazarnov.commands;

import ru.daniilazarnov.MessagePacket;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ShowFile extends Commands {

    @Override
    public MessagePacket runCommands(MessagePacket messagePacket) {

        String userDir=messagePacket.getUserDir();
        Path userPath = Paths.get(userDir);
        String homeDirectory=messagePacket.getHomeDirectory();
        Path homePath=Paths.get(homeDirectory);
        Path homePath1=userPath.resolve(homePath);
        if (!Files.exists(userPath)) try {
            Files.createDirectories(userPath);
            System.out.println("User directory is created!");
        } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
        }
        if (!Files.exists(userPath.resolve(homePath))) try {
            homePath = Files.createDirectories(userPath.resolve(homePath));
            System.out.println("User home directory is created!" + homeDirectory);
        } catch (IOException e) {
            System.err.println("Failed to create user Home directory!" + e.getMessage());
        }

        Path path = userPath;
        List<String> paths = null;
        try {
            paths = listFiles(path, homePath1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] newContent = paths.toString().getBytes();
        messagePacket.setContent(newContent);
        messagePacket.setMessage(paths);

        return messagePacket;
    }

    @Override
    public MessagePacket runClientCommands(MessagePacket messagePacket) {
        System.out.println("-------------------------------------------------------------");
        System.out.println("На сервер загружены следующие файлы:");
        List<String> answer=messagePacket.getMessage();
        answer.stream()
                .map(s -> "- "+s)
                .forEach(System.out::println);
        System.out.println("-------------------------------------------------------------\n");

        return null;
    }

    public static List<String> listFiles(Path path, Path homePath) throws IOException {
        List<String> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile)
                    .map(path1 -> path1.getName(path1.getNameCount() - 1))
                    .map(path1 -> path1.toString())
                    .collect(Collectors.toList());
        }              return result;
    }
    @Override
    public MessagePacket runOutClientCommands(Scanner scanner, MessagePacket messagePacket) {
        return messagePacket;
    }

}