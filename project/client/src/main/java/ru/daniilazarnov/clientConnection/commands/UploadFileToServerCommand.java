package ru.daniilazarnov.clientConnection.commands;

import ru.daniilazarnov.Protocol;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadFileToServerCommand implements ICommand {
    private final String[] args;

    public UploadFileToServerCommand(ArgumentsForCommand arguments) {
        this.args = arguments.getArgs();
    }

    @Override
    public boolean apply(SocketChannel socketChannel) throws IOException {
        if (socketChannel == null) {
            return false;
        }
        Path pathSrcFile = Paths.get(args[0]);
        if (!Protocol.sendFileToSocketChannel(pathSrcFile, socketChannel)) {
            System.out.println("File not found");
            return false;
        }
        return true;
    }
}
