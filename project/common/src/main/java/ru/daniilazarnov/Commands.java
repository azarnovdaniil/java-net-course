package ru.daniilazarnov;

import ru.daniilazarnov.commands.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public enum Commands {

    UPLOAD("upload", (byte) 1, new CommandUpload(), "загрузка файл на сервер"),
    DOWNLOAD("download", (byte) 3, new CommandDownload(), "скачивание файл с сервера"),
    LS("ls", (byte) 2, new CommandLS(), "вывод списка файлов, расположенных на удаленном хранилище"),
    LLS("lls", (byte) 4, new CommandLocalLS(), "вывод списка файлов, расположенных в локальном хранилище"),
    RM("rm", (byte) 8, new CommandRM(), "удаление файла из локальной папки"),
    HELP("help", (byte) 9, new CommandHelp(), "help info"),
    RENAME("rename", (byte) 11, new CommandRename(), "переименовать файл"),
    EXIT("exit", (byte) 12, new CommandExit(), "выход из приложения"),
    UNKNOWN("unknown", Byte.MIN_VALUE, new CommandUnknown(), "неизвестная команда");

    private static final Map<Byte, Commands> COMMANDS_MAP = Arrays.stream(Commands.values())
            .collect(Collectors.toMap(commands -> commands.signal, Function.identity()));
    private final byte signal;
    private final String name;
    private final Command commandApply;
    private final String helpInfo;

    Commands(String name, byte signal, Command command, String helpInfo) {
        this.name = name;
        this.signal = signal;
        this.commandApply = command;
        this.helpInfo = helpInfo;
    }

    public static String help() {
        return Arrays.stream(Commands.values())
                .map(commands -> commands.helpInfo)
                .collect(Collectors.joining("\n"));
    }

    public void sendToServer(ChannelHandlerContext ctx, String readLine, FileWorker fileWorker) {
        commandApply.send(ctx, readLine, fileWorker, signal);
    }

    public void receiveAndSend(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles) {
        commandApply.response(ctx, buf, fileWorker, uploadedFiles, signal);
    }

    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles) {
        commandApply.receive(ctx, buf, fileWorker, uploadedFiles);
    }

    public static Commands getCommand(byte code) {
        return COMMANDS_MAP.getOrDefault(code, UNKNOWN);
    }
}
