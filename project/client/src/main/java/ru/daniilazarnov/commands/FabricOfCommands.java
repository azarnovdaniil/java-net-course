package ru.daniilazarnov.commands;

import ru.daniilazarnov.CommandsToServer;
import ru.daniilazarnov.Protocol;

import java.text.DateFormat;


public class FabricOfCommands {

    public static ICommands getCommand(ArgumentsForCommand arguments) {
        byte numberOfCommand = arguments.getNumberOfCommand();
        switch (numberOfCommand) {
            case Protocol.STOR:
                return new UploadFileCommand(arguments);
            case Protocol.MKD:
                return new MkDirCommand(arguments);
            case Protocol.CD:
                return new ChangeDirCommand(arguments);
            case Protocol.RETR:
                return new DownloadFileCommand(arguments);
            case Protocol.USER:
                return new AuthCommand(arguments);
            case Protocol.HELP:
                return new ShowHelpCommand(arguments);
            case Protocol.CONNECT:
                return new ConnectToServerCommand(arguments);
            default:
                return null;
        }
    }
}
