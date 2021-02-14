package ru.daniilazarnov;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum Commands {

    UNKNOWN("unknown", Byte.MIN_VALUE, "неизвестная команда"),
    EXIT("exit", (byte) 0, "разорвать соединение и выйти из приложения"),
    ULF("ulf", (byte) 1, "загрузка файл на сервер"),
    DLF("dlf", (byte) 2, "скачивание файл с сервера"),
    FLS("fls", (byte) 3, "вывод списка файлов, расположенных в локальном хранилище"),
    FLS_SERVER("", (byte) 4, "вывод списка файлов, расположенных на удаленном хранилище"),
    RM_CLIENT("rmc", (byte) 5, "удаление файла из локальной папки"),
    RM_SERVER("rms", (byte) 6, ""),
    HELP("help", (byte) 7, "Список команд");

    private final byte signal;
    private final String nameCommand;
    private final String helpInfo;
    private static final String HOME_FOLDER_PATH = "project/client/local_storage/";


    Commands(String nameCommand, byte signal, String helpInfo) {
        this.signal = signal;
        this.nameCommand = nameCommand;
        this.helpInfo = helpInfo;
    }

    public static String help() {
        return Arrays.stream(Commands.values())
                .map(commands -> commands.helpInfo)
                .collect(Collectors.joining("\n"));
    }

    private static Commands getCommandByte(byte b) {
        return Arrays.stream(Commands.values())
                .filter(command -> command.getCommandByte() == b).findFirst().orElse(Commands.UNKNOWN);
    }

    public static Commands valueOf(byte readed) {
        return getCommandByte(readed);
    }

    public byte getCommandByte() {
        return signal;
    }

    private static boolean isThereaSecondElement(String inputLine) {
        return inputLine.split(" ").length == 2;
    }

    public String getFilesListFromLocalDirectory(String inputLine) {
        String result = "";
        String fileName;
        if (isThereaSecondElement(inputLine)) {
            fileName = inputLine.split(" ")[1];
            if (!Files.isDirectory(Path.of(HOME_FOLDER_PATH + fileName))) {
                return "Файл не является каталогом";
            }
        } else fileName = "";
        try {
            result = UtilMethod.getFolderContents(fileName, "user"); // TODO: 09.02.2021
        } catch (IOException e) {
            ;
        }
        return result;
    }
}
