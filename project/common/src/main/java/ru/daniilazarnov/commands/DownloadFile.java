package ru.daniilazarnov.commands;

import ru.daniilazarnov.MessagePacket;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public final class DownloadFile extends Commands {
    public DownloadFile(String s) {
        this.messageForInput = s;
    }

    @Override
    public MessagePacket runCommands(MessagePacket messagePacket) {
        String userDir;
        String homeDirectory;
        Path homePath;
        String fileName;
        Path filePath;
        String outMessages = ""; //сообщение, возвращаемое пользователю по итогам операции скачивания
        String outMessagesError = ""; //сообщение, возвращаемое пользователю при ошибке скачинвания

        System.out.println("Поступила команда скачать файл \"" + messagePacket.getFileName() + "\" в директорию: " + messagePacket.getHomeDirectory());
        userDir = messagePacket.getUserDir();
        homeDirectory = messagePacket.getHomeDirectory();
        homePath = Paths.get(userDir, homeDirectory);
        fileName = messagePacket.getFileName();
        filePath = Paths.get(userDir, homeDirectory, fileName);
        System.out.println(filePath);

        if (!Files.exists(filePath)) {
            outMessagesError = "Ошибка: введенное имя файла не найдено на диске, пожалуйста, повторите ввод команды.";
            System.out.println(outMessages);
            messagePacket.setFileName(fileName);
            messagePacket.setContent(null);
        } else {
            messagePacket.setFileName(fileName);
            try {
                messagePacket.setContent(Files.readAllBytes(filePath));
                outMessages = "С сервера скачан следующий файл: ";
                System.out.println(outMessages);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        messagePacket.setMessage(List.of(outMessagesError, outMessages, homePath.relativize(filePath).toString()));
        System.out.println(messagePacket.getMessage());
        return messagePacket;
    }

    @Override
    public MessagePacket runClientCommands(MessagePacket messagePacket) {
        Path newFilePath = null;
        Path oldFilePath = null;
        String homeDirectory;
        Path homePath;
        String fileName;
        Path filePath;
        String outMessages = ""; //сообщение, возвращаемое пользователю по итогам операции скачивания
        String outMessagesError = ""; //сообщение, возвращаемое пользователю при ошибке скачинвания

        homeDirectory = messagePacket.getHomeDirectory();
        homePath = Paths.get(homeDirectory);
        fileName = messagePacket.getFileName().replaceAll("(?:[a-zA-Z]:)\\([\\w-]+\\)*\\w([\\w-.])+", "");
        filePath = Paths.get(homeDirectory, fileName);
        System.out.println(filePath);
        System.out.println(homePath);
        oldFilePath = filePath;

        if (!Files.exists(homePath)) try {
            Files.createDirectories(homePath);
            System.out.println("Для загрузки /скачивания файлов используйте папку!");
            System.out.println(homePath.toAbsolutePath());
        } catch (
                IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
        }
        if (messagePacket.getContent() != null) {
            try {
                if (Files.exists(filePath)) { //если путь уже существует, то ищем несуществующий путь для сохранения в него существующего файла

                    while (Files.exists(oldFilePath)) {
                        oldFilePath = Path.of(oldFilePath + "old");
                    }

                    Files.copy(filePath, oldFilePath, StandardCopyOption.REPLACE_EXISTING);
                    Files.delete(filePath);
                    outMessagesError = "При скачивании файла обнаружен конфликт: " +
                            "файл с таким именем уже существует в вашей директории.\n" +
                            "Конфликт разрешен: файлу вашей директории присвоено новое имя " +
                            homePath.relativize(oldFilePath) + ".\n";
                    System.out.println(outMessagesError);
                }
                newFilePath = Files.createFile(filePath);
                Files.write(newFilePath, messagePacket.getContent(), StandardOpenOption.WRITE);
                outMessages = "В вашу директорию скачан файл: ";
                System.out.println(outMessages);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
            String fileName = scanner.nextLine().trim();
            messagePacket.setFileName(fileName);
        }
        return messagePacket;
    }
}

