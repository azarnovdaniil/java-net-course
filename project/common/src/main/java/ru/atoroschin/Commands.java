package ru.atoroschin;

import ru.atoroschin.commands.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Commands {
    UPLOAD((byte) 1, new CommandUpload(),
            " <имя файла из текущей локальной директории> - загрузить указанный файл в облако."),
    DOWNLOAD((byte) 3, new CommandDownload(),
            " <имя файла из текущей директории в облаке> - скачать указанный файл. Файл будет "
                    + "скачан в текущую локальную директорию."),
    LS((byte) 2, new CommandLS(),
            " - показать список файлов и директорий в текущей директории."),
    CD((byte) 6, new CommandCD(),
            " <имя директории> - перейти в директорию с указанным именем."),
    MKDIR((byte) 7, new CommandMkDir(),
            " <имя директории> - создать директорию с указанным именем."),
    RM((byte) 8, new CommandRM(),
            " <имя файла или директории> - удалить файл или директорию (если директория не пустая, "
                    + "она не будет удалена)."),
    MV((byte) 10, new CommandMV(), " <имя файла> <имя директории> - переместить файл "
            + "в указанную директорию. Директория указывается от текущей."),
    RENAME((byte) 11, new CommandRename(),
            " <имя файла> <новое имя файла> - переименовать файл."),
    FREESPACE((byte) 17, new CommandFreeSpace(),
            " - показать объем свободного места в облаке."),
    LLS((byte) 4, new CommandLocalLS(),
            " - показать список файлов и директорий в текущей локальной директории."),
    LCD((byte) 5, new CommandLocalCD(),
            " <имя директории> - перейти в локальную директорию с указанным именем."),
    LRM((byte) 13, new CommandLocalRM(),
            " <имя файла или директории> - удалить локально файл или директорию "
                    + "(если директория не пустая, она не будет удалена)."),
    LMKDIR((byte) 14, new CommandLocalMkDir(),
            " <имя директории> - создать локальную директорию с указанным именем."),
    LMV((byte) 15, new CommandLocalMV(),
            " <имя файла> <имя директории> - переместить файл в указанную директорию. Директория указывается"
                    + " от текущей."),
    LRENAME((byte) 16, new CommandLocalRename(),
            " <имя файла> <новое имя файла> - переименовать локальный файл."),
    EXIT((byte) 12, new CommandExit(),
            " - выход из приложения."),
    HELP((byte) 9, new CommandHelp(),
            " - показать команды."),
    UNKNOWN(Byte.MIN_VALUE, new CommandUnknown(),
            " - неизвестная команда.");

    private static final Map<Byte, Commands> COMMANDS_MAP = Arrays.stream(Commands.values())
            .collect(Collectors.toMap(commands -> commands.signal, Function.identity()));
    private final byte signal;
    private final String description;
    private final Command commandApply;

    Commands(byte signal, Command command, String description) {
        this.signal = signal;
        this.commandApply = command;
        this.description = description;
    }

    public void sendToServer(ChannelHandlerContext ctx, String readLine, FileWorker fileWorker) {
        commandApply.send(ctx, readLine, fileWorker, signal);
    }

    public void receiveAndSend(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles) {
        commandApply.response(ctx, buf, fileWorker, uploadedFiles, signal);
    }

    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles) throws IOException {
        commandApply.receive(ctx, buf, fileWorker, uploadedFiles);
    }

    public static Commands getCommand(byte code) {
        return COMMANDS_MAP.getOrDefault(code, UNKNOWN);
    }

    public String getDescription() {
        return description;
    }

    public byte getSignal() {
        return signal;
    }
}
