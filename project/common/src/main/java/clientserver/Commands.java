package clientserver;

import clientserver.commands.CommandLS;
import clientserver.commands.CommandUnknown;
import clientserver.commands.CommandUpload;
import io.netty.channel.ChannelHandlerContext;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Commands {
    UPLOAD("upload", (byte) 1, new CommandUpload()),  // [команда][имя файла на клиенте][путь назначения на сервере][размер файла][файл][][][]
    LS("ls", (byte) 3, new CommandLS()), // [команда]  // [ответ][список файлов ???]
    //    DOWNLOAD("download", (byte) 2),
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
        commandApply.apply(ctx, readLine, signal);
    }

    public byte getSignal() {
        return signal;
    }

    public static Commands getCommand(byte code) {
        return commandsMap.getOrDefault(code, UNKNOWN);
    }
}
