package ru.daniilazarnov.commands;

import ru.daniilazarnov.ClientConnection;
import ru.daniilazarnov.Commands;
import ru.daniilazarnov.Protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadFileToServer implements ICommand {
    private final String[] args;
    private final Commands command = Commands.stor;
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public UploadFileToServer(ArgumentsForCommand arguments) {
        this.args = arguments.getArgs();
    }

    @Override
    public boolean apply(ClientConnection connection) throws IOException {
        SocketChannel socketChannel = connection.getClientSocketChannel();
        if (socketChannel == null) {
            return false;
        }
        if (args.length != 1) {
            System.out.println("Wrong command");
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
