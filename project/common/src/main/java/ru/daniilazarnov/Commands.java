package common;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Commands {
    LS((byte)1, "ls-данные по каталогам" ),
    UPLOAD((byte) 2,"upload-Загрузка на сервер - еще не доделано"),
    DOWNLOAD((byte) 3,"download-Загрузка с сервера"),
    DELETE((byte) 4,"delete-Удаление- еще не доделано"),
    RENAME((byte) 5,"rename-Переименование файла- еще не доделано"),
    HELP((byte) 6,"help-помощь");

    private final byte commandByte;
    private final String help;

    public String getHelp() {
        return help;
    }

    public static String helpInfo() {
        return Arrays.stream(Commands.values())
                .map(Commands::getHelp)
                .collect(Collectors.joining("\n"));
    }

    private static Commands getCommandByte(byte b) {
        return Arrays.stream(Commands.values())
                .filter(command -> command.getCommandByte() == b).findFirst().orElse(Commands.HELP);
    }

    Commands(byte commandByte, String help) {
        this.commandByte = commandByte;
        this.help = help;
    }

    public static Commands valueOf(byte readed) {
        return getCommandByte(readed);
    }
    public byte getCommandByte() {
        return commandByte;
    }

}