package ru.daniilazarnov;

import ru.daniilazarnov.commands.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.SocketHandler;

public class User {
    private final String clientName;
    private final String clientKEY;
    private HashMap<String, Commands> commandsMap; //список команд, которыми располагает пользователь
    private String command;


    public HashMap<String, Commands> getCommandsMap() {
        commandsMap = new HashMap<>();
        commandsMap.put("*UPLOAD", new UploadFile("Ведите имя файла для загрузки на сервер:"));
        commandsMap.put("*DOWNLOAD", new DownloadFile("Ведите имя файла для скачивания:"));
        commandsMap.put("*SHOW", new ShowFile());
        commandsMap.put("*DELETE", new DeleteFile("Ведите имя файла для его удаления на сервере:"));
        commandsMap.put("*RENAME", new RenameFile("Для переименования ведите: /номер файла (или его имя) -> новое имя файла, например: /12 -> newText или text -> newtext"));
        commandsMap.put("*EXIT", new Exit());
        return commandsMap;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientKEY() {
        return clientKEY;
    }

    public User(String clientName, String clientKEY) {
        this.clientName = clientName;
        this.clientKEY = clientKEY;
        commandsMap = getCommandsMap(); //инициализируем список команд пользователя

    }

    public MessagePacket invoke(Scanner scanner, MessagePacket messagePacket) {

        Commands userCommand;
        System.out.println("Пожалуйста, введите вашу команду: ");
        command = scanner.nextLine().toUpperCase().trim();
        while (!commandsMap.containsKey(command)) {
            System.out.println("Ошибка: введеннная команда не распознана, попробуйте еще раз");
            command = scanner.nextLine().toUpperCase().trim();
        }
        userCommand = commandsMap.get(command); // получаем объект команды
        messagePacket.setCommand(userCommand); //добавляем команду в пакет
        messagePacket = userCommand.runOutClientCommands(scanner, messagePacket); //получаем дополнительную информацию для подготовки пакета
        return messagePacket;
    }

    void setHomeFolder(String homeFolder) {
        try {
            Path homePath = Path.of(homeFolder.replaceAll("(?:[a-zA-Z]:)\\([\\w-]+\\)*\\w([\\w-.])+", ""));
            if (!Files.exists(homePath))
                Files.createDirectories(homePath);
            System.out.println("Для загрузки /скачивания файлов используйте папку:");
            System.out.println(homePath.toAbsolutePath());
            System.out.println();
        } catch (
                IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
        }
    }

}
