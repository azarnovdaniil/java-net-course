package ru.daniilazarnov.commands;

import ru.daniilazarnov.MessagePacket;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DeleteFile extends Commands {

    @Override
    public String getMessageForInput() {
        return messageForInput;
    }

    public DeleteFile(String s) {
        this.messageForInput = s;
    }

    @Override
    public MessagePacket runCommands(MessagePacket messagePacket) {
        List<String> fileServerNames = messagePacket.getMessage();
        Path homePath = Path.of(messagePacket.getUserDir(), messagePacket.getHomeDirectory());

        String serverFileName = fileServerNames.get(0);
        try {
            serverFileName = serverFileName.startsWith("/") ? getFileName(homePath, serverFileName.substring(1)) : serverFileName;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Поступила команда удалить файл " + serverFileName);
        Path oldFilePath = Path.of(messagePacket.getUserDir(), messagePacket.getHomeDirectory(), serverFileName);

        String answerHeadLines = "-------------------------------------------------------------\n";
        String subjectLines = "";
        try {
            if (Files.exists(oldFilePath)) { //если путь уже существует, то ищем несуществующий путь для сохранения в него существующего файла
                Files.delete(oldFilePath);
                subjectLines = "На сервере удален файл:\n" + serverFileName;
            } else {
                subjectLines = "При удалении файла возникла ошибка, проверьте корректность имени файла: " + serverFileName;

            }
        } catch (IOException | InvalidPathException e) {
            e.printStackTrace();
        }
        String answerFooterLines = "\n-------------------------------------------------------------\n";
        List<String> answer = List.of(answerHeadLines, subjectLines, answerFooterLines);
        messagePacket.setMessage(answer);
        return messagePacket;
    }

    private String getFileName(Path homePath, String substring) throws IOException {

        List<String> listFiles;
        try (Stream<Path> walk = Files.walk(homePath)) {
            listFiles = walk.filter(Files::isRegularFile)
                    .map(path1 -> path1.getName(path1.getNameCount() - 1))
                    .map(Path::toString)
                    .collect(Collectors.toList());
        }
        return listFiles.get(Integer.parseInt(substring));


    }

    @Override
    public MessagePacket runClientCommands(MessagePacket messagePacket) {
        List<String> answer = messagePacket.getMessage();
        answer
                .forEach(System.out::print);
        return null;
    }


    @Override
    public MessagePacket runOutClientCommands(Scanner scanner, MessagePacket messagePacket) {
        if (this.messageForInput != null) {
            System.out.println(this.messageForInput);
            String fileNames = scanner.nextLine().trim().replaceAll(" ", "");
            List<String> fileServerNames = Collections.singletonList(fileNames);
            messagePacket.setMessage(fileServerNames);
        }
        return messagePacket;
    }

}
