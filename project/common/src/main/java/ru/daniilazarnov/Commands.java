package ru.daniilazarnov;

import java.util.Arrays;
import java.util.Optional;

public enum Commands {
    stor((byte) 1),
    mkd((byte) 2),
    cd((byte) 3),
    retr((byte) 4),
    user((byte) 5),
    connect((byte) 6),
    help((byte) 7),
    pwd_or_string((byte) 8),;

    private byte numberOfCommand;

    Commands(byte numberOfCommand) {
        this.numberOfCommand = numberOfCommand;
    }

    public byte getNumberOfCommand() {
        return numberOfCommand;
    }

    public static Optional<Commands> getCommand(byte number) {
        return Arrays.stream(Commands.values()).filter(x -> x.numberOfCommand == number).findAny();
    }


}
