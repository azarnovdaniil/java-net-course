package ru.daniilazarnov;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Command {
    UNKNOWN((byte) -1, ""),
    EXIT((byte) 0, "'exit' - разорвать соединение и выйти из приложения\n"),
    UPLOAD((byte) 1, "'upload' - загружает файл на сервер\n"),
    DOWNLOAD((byte) 2, "'download' - скачивает файл с сервера\n"),
    CONNECT((byte) 3, "'connect' - если соединение разорвано - восстанавливает его\n"),
    LS((byte) 4, "'ls' - вывести имена файлов и каталогов расположенных в корне папки пользователя в локальном "
            + "хранилище\n"
            + "'ls [catalog_name]' - вывести имена файлов и каталогов расположенных в папке \n"),
    STATUS((byte) 5, "'status' - вывести статус подключения к серверу\n"),
    AUTH((byte) 6, "'auth' - Авторизация\n"),
    HELP((byte) 7, "'help' - Помощь\n"),
    DISCONNECT((byte) 8, "'diconnect' - Разорвать соединение\n"),
    SERVER((byte) 9, "'server ls [catalog_name]' - вывести имена файлов и каталогов расположенных в папке \n"
            + "[catalog_name] на удаленном хранилище\n");

    private final byte commandByte;
    private final String helpInfo;

    public static String getHelpInfo() {
        return Arrays.stream(Command.values())
                .map(Command::toString)
                .collect(Collectors.joining("", "\nКоманды поддерживаемые приложением:\n", "Пример:\n"
                        + "'upload fileclient'\n"
                        + "'download fileserver'\n"
                        + "'server ls'\n"));
    }

    @Override
    public String toString() {
        return helpInfo;
    }

    private static Command getCommandByte(byte b) {
        return Arrays.stream(Command.values())
                .filter(command -> command.getCommandByte() == b).findFirst().orElse(Command.UNKNOWN);
    }

    public static Command valueOf(byte readed) {
        return getCommandByte(readed);
    }

    public byte getCommandByte() {
        return commandByte;
    }

    Command(byte commandByte, String helpInfo) {
        this.commandByte = commandByte;
        this.helpInfo = helpInfo;
    }
}
