package ru.daniilazarnov.clientConnection.commands;

import ru.daniilazarnov.Commands;

import java.util.Arrays;

public final class ArgumentsForCommand {
    private final Commands command;
    private final String[] args;
    private static final Commands[] COMMANDS = Commands.values();

    public ArgumentsForCommand(Commands command, String[] args) {
        this.command = command;
        this.args = args;
    }

    public static ArgumentsForCommand getArguments(String[] messageFromCLI) {
        for (Commands command : COMMANDS) {
            if (String.valueOf(command).equals(messageFromCLI[0])) {
                return new ArgumentsForCommand(command, Arrays.copyOfRange(messageFromCLI, 1, messageFromCLI.length));
            }
        }
        return new ArgumentsForCommand(Commands.unknown, Arrays.copyOfRange(messageFromCLI, 1, messageFromCLI.length));
    }

    public String[] getArgs() {
        return args;
    }

    public Commands getCommand() {
        return this.command;
    }
}
