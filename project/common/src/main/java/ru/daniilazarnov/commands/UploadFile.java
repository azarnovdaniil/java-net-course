package ru.daniilazarnov.commands;

import ru.daniilazarnov.MessagePacket;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;

public final class UploadFile extends Commands {

    String fileName = "";

    @Override
    public String getMessageForInput() {
        return messageForInput;
    }

    public UploadFile(String s) {
        this.messageForInput = s;

    }

    @Override
    public MessagePacket runCommands(MessagePacket messagePacket) {
        String outMessages = ""; //сообщение, возвращаемое пользователю по итогам операции
        String outMessagesOver = ""; //сообщение, возвращаемое пользователю по итогам операции при перезаписи файла
        Path newFilePath;
        Path oldFilePath;
        System.out.println("Поступила команда загрузить файл \"" + messagePacket.getFileName() + "\" в директорию: " + messagePacket.getHomeDirectory());
        String userDir = messagePacket.getUserDir();
        String homeDirectory = messagePacket.getHomeDirectory();
        Path homePath = Paths.get(userDir, homeDirectory);
        String fileName = messagePacket.getFileName();
        fileName = fileName.replaceAll("(?:[a-zA-Z]:)\\([\\w-]+\\)*\\w([\\w-.])+", "");
        System.out.println(fileName);
        Path filePath = Paths.get(userDir, homeDirectory, fileName);
        System.out.println(filePath);
        oldFilePath = filePath;

        try {

            if (Files.exists(oldFilePath)) { //если путь уже существует, то ищем несуществующий путь для сохранения в него существующего файла

                while (Files.exists(oldFilePath)) {
                    oldFilePath = Path.of(oldFilePath + "old");
                }

                Files.copy(filePath, oldFilePath, StandardCopyOption.REPLACE_EXISTING);
                Files.delete(filePath);
                outMessagesOver = "При загрузке файла обнаружен конфликт: " +
                        "файл с таким именем уже существует на Сервере.\n" +
                        "Конфликт разрешен: файлу на Сервере присвоено новое имя " +
                        homePath.relativize(oldFilePath) + ".\n";
            }
            newFilePath = Files.createFile(filePath);
            Files.write(newFilePath, messagePacket.getContent(), StandardOpenOption.WRITE);
            outMessages = "На сервер загружены следующие файлы: ";
        } catch (IOException | InvalidPathException e) {
            e.printStackTrace();
        }

        messagePacket.setContent(null);
        messagePacket.setMessage(List.of(outMessagesOver, outMessages, homePath.relativize(filePath).toString()));
        return messagePacket;
    }

    @Override
    public MessagePacket runClientCommands(MessagePacket messagePacket) {
        System.out.println("-------------------------------------------------------------");
        List<String> answer = messagePacket.getMessage();
        answer
                .forEach(System.out::println);
        System.out.println("-------------------------------------------------------------\n");

        return null;
    }

    @Override
    public MessagePacket runOutClientCommands(Scanner scanner, MessagePacket messagePacket) {
        if (this.messageForInput != null) {
            System.out.println(this.messageForInput);
            fileName = scanner.nextLine().trim();

            Path path = Paths.get(messagePacket.getHomeDirectory(), fileName);
            while (!Files.exists(path)) {
                System.out.println("Ошибка: введенное имя файла не найдено на диске, пожалуйста, повторите ввод:");
                fileName = scanner.nextLine().trim();
                path = Paths.get(messagePacket.getHomeDirectory(), fileName);
            }
            messagePacket.setFileName(fileName);
            System.out.println("На сервер загружается файл: " + path);
            try {
                messagePacket.setContent(Files.readAllBytes(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        messagePacket.setSenDToServer(true);
        return messagePacket;
    }
}
