package ru.daniilazarnov.commands;

import ru.daniilazarnov.Commands;

import java.util.Arrays;

public class ArgumentsForCommand {
    private final Commands command;
    private String[] args;

    private static Commands[] commands = Commands.values();

    private ArgumentsForCommand(Commands command, String args[]) {
        this.command = command;
        this.args = args;
    }

    public static ArgumentsForCommand getArguments(String[] messageFromCLI) {
        for (Commands command : commands) {
            if(String.valueOf(command).equals(messageFromCLI[0])) {
                return new ArgumentsForCommand(command, Arrays.copyOfRange(messageFromCLI, 1, messageFromCLI.length));
            }
        }
        return null;
    }

    public String[] getArgs() {
        return args;
    }

    public Commands getCommand() {
        return this.command;
    }
}
