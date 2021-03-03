package ru.daniilazarnov;

import ru.daniilazarnov.commands.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class User {
    private final String clientName;
    private final String clientKEY;
    private HashMap<String, Commands> commandsMap; //список команд, которыми располагает пользователь

    public HashMap<String, Commands> getCommandsMap() {
        commandsMap = new HashMap<>();
        commandsMap.put("*UPLOAD", new UploadFile("Ведите имя файла для загрузки на сервер:"));
        commandsMap.put("*DOWNLOAD", new DownloadFile("Ведите имя файла для скачивания:"));
        commandsMap.put("*SHOW", new ShowFile());
        commandsMap.put("*DELETE", new DeleteFile("Ведите имя файла для его удаления на сервере:"));
        commandsMap.put("*RENAME", new RenameFile("Ведите (через запятую) \"текущее имя файла\" и \"новое имя файла\"  для переименования:"));
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

    public MessagePacket invoke(Scanner scanner, MessagePacket messagePacket, boolean next) {
        Commands userCommand;
        String command;
        System.out.println("Пожалуйста, введите вашу команду: ");
        command = scanner.nextLine().toUpperCase().trim();
        while (!commandsMap.containsKey(command)) {
            System.out.println("Ошибка: введеннная команда не распознана, попробуйте еще раз");
            command = scanner.nextLine().toUpperCase().trim();
        }
        userCommand = commandsMap.get(command);
        messagePacket.setCommand(userCommand);
        if (userCommand.getMessageForInput() != null) {
            System.out.println(userCommand.getMessageForInput());
            String fileName = scanner.nextLine().trim();
            messagePacket.setContent(scanner, fileName, null);
        }

        return messagePacket;
    }

}
