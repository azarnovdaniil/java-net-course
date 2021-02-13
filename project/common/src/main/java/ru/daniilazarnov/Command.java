package ru.daniilazarnov;

import java.util.Arrays;

public enum Command {
    UNKNOWN((byte) -1),
    EXIT((byte) 0),
    UPLOAD((byte) 1),
    DOWNLOAD((byte) 2),
    CONNECT((byte) 3),
    LS((byte) 4),
    STATUS((byte) 5),
    AUTH((byte) 6),
    HELP((byte) 7);

   private byte commandByte;

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

    Command(byte commandByte) {
        this.commandByte = commandByte;
    }
}
