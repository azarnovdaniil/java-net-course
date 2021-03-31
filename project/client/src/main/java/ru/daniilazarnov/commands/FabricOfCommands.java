package ru.daniilazarnov.commands;

import ru.daniilazarnov.Commands;


public class FabricOfCommands {

    public static ICommands getCommand(ArgumentsForCommand arguments) {
        Commands command = arguments.getCommand();
        switch (command) {
            case stor:
                return new UploadFileCommand(arguments);
            case mkd:
                return new MkDirCommand(arguments);
            case cd:
                return new ChangeDirCommand(arguments);
            case retr:
                return new DownloadFileCommand(arguments);
            case user:
                return new AuthCommand(arguments);
            case help:
                return new ShowHelpCommand(arguments);
            case connect:
                return new ConnectToServerCommand(arguments);
            default:
                return null;
        }
    }
}
