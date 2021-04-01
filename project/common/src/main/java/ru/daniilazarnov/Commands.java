package ru.daniilazarnov;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Commands {
//    LS((byte)1, ""),
    DOWNLOAD((byte) 1, "[download] [destination]"),
    UPLOAD((byte) 2, "[upload] [source]"),
    HELP((byte) 3, "[help] - show all commands");


    private final byte commBytes;
    private final String explain;


    Commands(byte commBytes, String explain) {
        this.commBytes = commBytes;
        this.explain = explain;
    }

    public String getExplain() {
        return explain;
    }

    public byte getCommBytes() {
        return commBytes;
    }

    public static Commands getCommand(byte b) {
        return getCommBytes(b);
    }

    private static Commands getCommBytes(byte b) {
        return Arrays.stream(Commands.values())
                .filter(command -> command.getCommBytes() == b)
                .findFirst().orElse(Commands.HELP);
    }

    public String helpInfo() {
        return Arrays.stream(Commands.values())
                .map(Commands::getExplain)
                .collect(Collectors.joining("\n"));
    }

}
