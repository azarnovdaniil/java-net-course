package ru.daniilazarnov.commands;

import ru.daniilazarnov.CommandsToServer;

import java.util.Arrays;

public class ArgumentsForCommand {
    private byte numberOfCommand;
    private String[] args;

    private static CommandsToServer[] commands = CommandsToServer.values();

    private ArgumentsForCommand(CommandsToServer command, String args[]) {
        this.numberOfCommand = command.getNumberOfCommand();
        this.args = args;
    }

    public static ArgumentsForCommand getArguments(String[] messageFromCLI) {
        for (CommandsToServer command : commands) {
            if(String.valueOf(command).equals(messageFromCLI[0])) {
                return new ArgumentsForCommand(command, Arrays.copyOfRange(messageFromCLI, 1, messageFromCLI.length));
            }
        }
        return null;
    }

    public String[] getArgs() {
        return args;
    }

    public byte getNumberOfCommand() {
        return numberOfCommand;
    }
}
