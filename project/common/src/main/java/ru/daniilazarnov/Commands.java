package ru.daniilazarnov;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Commands {
    EMPTY((byte) -1, "empty"),
    FILE((byte)15, "file"),
    DOWNLOAD((byte) 16, "[download] [destination]"),
    UPLOAD((byte) 17, "[upload] [source]"),
    HELP((byte) 18, "[help] - show all commands"),
    DELETE((byte)19, "delete"),
    END((byte)20, "end"),
//    AUTH((byte)21, "auth"),
//    REG((byte)22, "reg"),
    CREATE((byte)23, "create"),
    FORWARD((byte)24, "forward"),
    BACK((byte)25, "back");//,
//    EXIT((byte)26, "exit"),
//    WARNING((byte)27, "warning");


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

    public Commands getCommand(byte b) {
        return getCommBytes(b);
    }

    public Commands getCommBytes(byte b) {
        return Arrays.stream(Commands.values())
                .filter(command -> command.getCommBytes() == b)
                .findFirst().orElse(Commands.HELP);
    }

    public String helpInfo() {
        return Arrays.stream(Commands.values())
                .map(Commands::getExplain)
                .collect(Collectors.joining("\n"));
    }

    public static void main(String[] args) {
        Commands commands = Commands.DOWNLOAD;
        System.out.println(commands.getExplain());
        System.out.println(commands.getCommBytes());
        System.out.println(commands.getCommand((byte)20));
        System.out.println(commands.helpInfo());
//        System.out.println(commands.getExplain());
    }
}
