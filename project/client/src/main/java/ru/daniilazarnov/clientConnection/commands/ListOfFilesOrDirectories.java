package ru.daniilazarnov.clientConnection.commands;

import ru.daniilazarnov.Commands;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ListOfFilesOrDirectories implements ICommand {
    private final String[] args;
    private final Commands command = Commands.ls;
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public ListOfFilesOrDirectories(ArgumentsForCommand arguments) {
        this.args = arguments.getArgs();
    }

    @Override
    public boolean apply(SocketChannel socketChannel) throws IOException {
        if (socketChannel == null) {
            return false;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        byteBuffer.put(command.getNumberOfCommand());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        return true;
    }
}
