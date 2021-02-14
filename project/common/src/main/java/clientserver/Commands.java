package clientserver;

import clientserver.commands.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Commands {
    UPLOAD("upload", (byte) 1, new CommandUpload()),
    LS("ls", (byte) 2, new CommandLS()),
    DOWNLOAD("download", (byte) 3, new CommandDownload()),
    LLS("lls", (byte) 4, new CommandLocalLS()),
    LCD("lcd", (byte) 5, new CommandLocalCD()),
    CD("cd", (byte) 6, new CommandCD()),
    MKDIR("mkdir", (byte) 7, new CommandMkDir()),
    RM("rm", (byte) 8, new CommandRM()),
    HELP("help", (byte) 9, new CommandHelp()),
    MV("mv", (byte) 10, new CommandMV()),
    RENAME("rename", (byte) 11, new CommandRename()),
    EXIT("exit", (byte) 12, new CommandExit()),
    UNKNOWN("unknown", Byte.MIN_VALUE, new CommandUnknown());

    private static final Map<Byte, Commands> commandsMap = Arrays.stream(Commands.values())
            .collect(Collectors.toMap(commands -> commands.signal, Function.identity()));
    private final byte signal;
    private final String name;
    private final Command commandApply;

    Commands(String name, byte signal, Command command) {
        this.name = name;
        this.signal = signal;
        this.commandApply = command;
    }

    public void sendToServer(ChannelHandlerContext ctx, String readLine, FileWorker fileWorker) {
        commandApply.send(ctx, readLine, fileWorker, signal);
    }

    public void receiveAndSend(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer, FileLoaded> uploadedFiles) {
        commandApply.response(ctx, buf, fileWorker, uploadedFiles, signal);
    }

    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer, FileLoaded> uploadedFiles) {
        commandApply.receive(ctx, buf, fileWorker, uploadedFiles);
    }

    public static Commands getCommand(byte code) {
        return commandsMap.getOrDefault(code, UNKNOWN);
    }
}
