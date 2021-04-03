package ru.daniilazarnov;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.daniilazarnov.commands.*;

public class FabricOfCommandsTest {

    @Test
    public void testGetUploadCommand() {
        String[] messageFromCLI = {"stor"};
        ArgumentsForCommand arguments = ArgumentsForCommand.getArguments(messageFromCLI);
        Assertions.assertEquals(UploadFileToServerCommand.class, FabricOfCommands.getCommand(arguments).getClass());
    }

    @Test
    public void testGetMakeDirAuthCommand() {
        String[] messageFromCLI = {"mkd"};
        ArgumentsForCommand arguments = ArgumentsForCommand.getArguments(messageFromCLI);
        Assertions.assertEquals(MkDirCommand.class, FabricOfCommands.getCommand(arguments).getClass());
    }

    @Test
    public void testGetChangeDirCommand() {
        String[] messageFromCLI = {"cd"};
        ArgumentsForCommand arguments = ArgumentsForCommand.getArguments(messageFromCLI);
        Assertions.assertEquals(ChangeDirCommand.class, FabricOfCommands.getCommand(arguments).getClass());
    }

    @Test
    public void testGetDownloadCommand() {
        String[] messageFromCLI = {"retr"};
        ArgumentsForCommand arguments = ArgumentsForCommand.getArguments(messageFromCLI);
        Assertions.assertEquals(DownloadFileCommand.class, FabricOfCommands.getCommand(arguments).getClass());
    }

    @Test
    public void testGetAuthCommand() {
        String[] messageFromCLI = {"user"};
        ArgumentsForCommand arguments = ArgumentsForCommand.getArguments(messageFromCLI);
        Assertions.assertEquals(AuthCommand.class, FabricOfCommands.getCommand(arguments).getClass());
    }

    @Test
    public void testGetConnectCommand() {
        String[] messageFromCLI = {"connect"};
        ArgumentsForCommand arguments = ArgumentsForCommand.getArguments(messageFromCLI);
        Assertions.assertEquals(ConnectToServerCommand.class, FabricOfCommands.getCommand(arguments).getClass());
    }

    @Test
    public void testGetHelpCommand() {
        String[] messageFromCLI = {"help"};
        ArgumentsForCommand arguments = ArgumentsForCommand.getArguments(messageFromCLI);
        Assertions.assertEquals(ShowHelpCommand.class, FabricOfCommands.getCommand(arguments).getClass());
    }

    @Test
    public void testGetPresentDirCommand() {
        String[] messageFromCLI = {"pwd"};
        ArgumentsForCommand arguments = ArgumentsForCommand.getArguments(messageFromCLI);
        Assertions.assertEquals(PresentWorkDirectoryCommand.class, FabricOfCommands.getCommand(arguments).getClass());
    }

    @Test
    public void testGetDisconnectCommand() {
        String[] messageFromCLI = {"disconnect"};
        ArgumentsForCommand arguments = ArgumentsForCommand.getArguments(messageFromCLI);
        Assertions.assertEquals(DisconnectCommand.class, FabricOfCommands.getCommand(arguments).getClass());
    }

    @Test
    public void testGetUnknownCommand() {
        String[] messageFromCLI = {"unknown"};
        ArgumentsForCommand arguments = ArgumentsForCommand.getArguments(messageFromCLI);
        Assertions.assertEquals(UnknownCommand.class, FabricOfCommands.getCommand(arguments).getClass());
    }
}
