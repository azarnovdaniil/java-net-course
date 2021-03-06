package ru.daniilazarnov.commands;

import ru.daniilazarnov.MessagePacket;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class RenameFile extends Commands {


    public RenameFile(String s) {
        this.messageForInput = s;
    }

    @Override
    public MessagePacket runCommands(MessagePacket messagePacket) {

        List<String> fileServerNames = messagePacket.getMessage();

        String oldFileName = fileServerNames.get(0);

        oldFileName = oldFileName.startsWith("/") ? oldFileName.substring(1) : oldFileName;
        String newFileName = fileServerNames.get(1);

        System.out.println("Поступила команда переименовать файл \"" + oldFileName + "\" на \"" + newFileName + "\"");

        Path oldFilePath = Path.of(messagePacket.getUserDir(), messagePacket.getHomeDirectory(), oldFileName);
        Path newFilePath = Path.of(messagePacket.getUserDir(), messagePacket.getHomeDirectory(), newFileName);


        String answerHeaderLines = "-------------------------------------------------------------\n";
        String answerSubjectLines = "";
        try {
            if (Files.exists(oldFilePath)) { //если путь уже существует, то ищем несуществующий путь для сохранения в него существующего файла

                Files.copy(oldFilePath, newFilePath, StandardCopyOption.REPLACE_EXISTING);
                Files.delete(oldFilePath);
                answerSubjectLines = "На сервере переименованы следующие файлы:\n" + oldFileName + " -> " + newFileName;
            } else {
                answerSubjectLines = "При переименовании возникла ошибка, " +
                        "проверьте корректность имени файла повторите ввод команды." +
                        "Вы ввели: \" " + oldFileName + " -> " + newFileName + "\"";

            }
        } catch (IOException | InvalidPathException e) {
            e.printStackTrace();

        }
        String answerFooterLines = "\n-------------------------------------------------------------\n";
        List<String> answer = List.of(answerHeaderLines, answerSubjectLines, answerFooterLines);
        messagePacket.setMessage(answer);
        return messagePacket;
    }

    public MessagePacket runClientCommands(MessagePacket messagePacket) {
        List<String> answer = messagePacket.getMessage();
        answer
                .forEach(System.out::print);
        return null;
    }


    @Override
    public MessagePacket runOutClientCommands(Scanner scanner, MessagePacket messagePacket) {
        List<String> fileServerNames;
        if (this.messageForInput != null) {
            System.out.println(this.messageForInput);
            do {
                fileServerNames = getNames(scanner);
            }
            while (fileServerNames.get(0).equals("") || fileServerNames.size() < 2);
            messagePacket.setMessage(fileServerNames);
        }
        return messagePacket;
    }

    private List<String> getNames(Scanner scanner) {
        List<String> fileServerNames;
        String fileNames;
        fileNames = scanner.nextLine().trim().replaceAll(" ", "");
        fileServerNames = Arrays.asList(fileNames.split("->", 0));
        if (fileServerNames.get(0).equals("") || fileServerNames.size() == 1)
            System.err.println("Ошибка, повторите ввод имени!");
        return fileServerNames;
    }

}
