package ru.daniilazarnov;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.daniilazarnov.clientConnection.commands.ArgumentsForCommand;

public class ArgumentsForCommandTest {
    @Test
    public void testArgumentsForCommandGetStor() {
        String[] messageFromCLI = {"stor"};
        Assertions.assertEquals(Commands.stor, ArgumentsForCommand.getArguments(messageFromCLI).getCommand());
    }

    @Test
    public void testArgumentsForCommandGetMkdr() {
        String[] messageFromCLI = {"mkd"};
        Assertions.assertEquals(Commands.mkd, ArgumentsForCommand.getArguments(messageFromCLI).getCommand());
    }

    @Test
    public void testArgumentsForCommandGetCd() {
        String[] messageFromCLI = {"cd"};
        Assertions.assertEquals(Commands.cd, ArgumentsForCommand.getArguments(messageFromCLI).getCommand());
    }

    @Test
    public void testArgumentsForCommandGetRetr() {
        String[] messageFromCLI = {"retr"};
        Assertions.assertEquals(Commands.retr, ArgumentsForCommand.getArguments(messageFromCLI).getCommand());
    }

    @Test
    public void testArgumentsForCommandGetUser() {
        String[] messageFromCLI = {"user"};
        Assertions.assertEquals(Commands.user, ArgumentsForCommand.getArguments(messageFromCLI).getCommand());
    }

    @Test
    public void testArgumentsForCommandGetConnect() {
        String[] messageFromCLI = {"connect"};
        Assertions.assertEquals(Commands.connect, ArgumentsForCommand.getArguments(messageFromCLI).getCommand());
    }

}
