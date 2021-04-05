package ru.daniilazarnov.clientConnection.commands;

import ru.daniilazarnov.Commands;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class ShowHelpCommand implements ICommand {
    private final String[] args;
    private final Commands command = Commands.help;

    public ShowHelpCommand(ArgumentsForCommand arguments) {
        this.args = arguments.getArgs();
    }

    @Override
    public boolean apply(SocketChannel socketChannel) throws IOException {
        System.out.print("user <name> - authorization\n"
                + "stor <file location> - uploading a file to the server\n"
                + "retr <file name> - downloading a file from the server\n"
                + "mkd <directory name/path to directory> - creating a directory\n"
                + "cd <path to directory> - change directory");
        return true;
    }
}
