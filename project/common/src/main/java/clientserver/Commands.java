package clientserver;

import clientserver.commands.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Commands {
    UPLOAD("upload", (byte) 1, new CommandUpload()),  // [команда][имя файла на клиенте][путь назначения на сервере][размер файла][файл][][][]
    LS("ls", (byte) 2, new CommandLS()), // [команда]  // [ответ][список файлов ???]
//    DOWNLOAD("download", (byte) 3),
//    RM("rm", (byte) 4), // [команда][длина имени файла][имя файла] // [ответ]
//    MKDIR("mkdir", (byte) 5), // [команда][длина имени][имя директории]
//    MV("mv", (byte) 6), // [команда][]
//    CD("cd", (byte) 7),
//    RENAME("rename", (byte) 8),
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

    public void sendToServer(ChannelHandlerContext ctx, String readLine) {
        commandApply.send(ctx, readLine, signal);
    }

    public void receiveAndSend(ChannelHandlerContext ctx, ByteBuf buf, String currentDir, Map<Integer, FileLoaded> uploadedFiles) {
        commandApply.response(ctx, buf, currentDir, uploadedFiles);
    }

    public void receive(ChannelHandlerContext ctx, ByteBuf buf) {
        commandApply.receive(ctx, buf);
    }

    public byte getSignal() {
        return signal;
    }

    public static Commands getCommand(byte code) {
        return commandsMap.getOrDefault(code, UNKNOWN);
    }
}
