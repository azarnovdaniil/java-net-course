package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.daniilazarnov.commands.*;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Commands {
    /*
    UF - Upload file;
    UNKNOWN - default unknown command;
    */

    UF("UF", (byte) 1, new CommandUpload()),
    LCD("LCD", (byte) 2, new CommandLocalCD()),
    DOWNLOAD("DOWNLOAD", (byte) 3, new CommandDownload()),
    UNKNOWN("UNKNOWN", Byte.MIN_VALUE, new CommandUnknown());

    byte signal;
    String nameCommand;
    private final Command commandApply;

    private static final Map<Byte, Commands> commandsMap = Arrays.stream(Commands.values())
            .collect(Collectors.toMap(commands -> commands.signal, Function.identity()));

    Commands(String nameCommand, byte signal, Command command) {
        this.signal = signal;
        this.nameCommand = nameCommand;
        this.commandApply = command;
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
        return commandsMap.getOrDefault(code, UNKNOWN);
    }
}
