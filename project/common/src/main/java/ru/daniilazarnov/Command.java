package ru.daniilazarnov;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Command {
    LS("ls", (byte) 1),
    MKDIR("mkdir", (byte) 2),
    UPLOAD("upload", (byte) 3),
    REG("reg",(byte) 4),
    AUTH("auth",(byte) 5),
    DOWNLOAD("download",(byte) 6),
    RM("rm",(byte) 7),
    RN("rn",(byte) 8),
    HELP("help", (byte) 9),
    CD("cd", (byte) 10),
    UNKNOWN("unknown", Byte.MIN_VALUE);


    private static final Map<Byte, Command> commandsMap = Arrays.stream(Command.values())
            .collect(Collectors.toMap(command -> command.signal, Function.identity()));

    private final String name;

    private final byte signal;

    public byte getSignal() {
        return signal;
    }

    Command(String name, byte signal){
        this.name=name;
        this.signal=signal;
    }

    public static Command getCommand(byte signal){
        return commandsMap.getOrDefault(signal, UNKNOWN);
    }
}
