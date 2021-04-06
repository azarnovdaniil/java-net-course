package ru.daniilazarnov;

import java.util.Arrays;
import java.util.Objects;

public enum Command {
    EXIT("-exit"),
    TEST("-test"),
    UPLOAD("-upload"),
    UNKNOWN("");

    private final String cmd;

    Command(String cmd) {
        this.cmd = cmd;
    }

    public String getCmd() {
        return cmd;
    }

    public static Command byCmd(String cmd) {
        return Arrays.stream(Command.values())
                .filter(command -> Objects.equals(command.getCmd(), cmd))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
