package ru.daniilazarnov.clientConnection.commands;

import ru.daniilazarnov.Commands;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class DownloadFileCommand implements ICommand {
    private final String[] args;
    private final Commands command = Commands.retr;
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public DownloadFileCommand(ArgumentsForCommand arguments) {
        this.args = arguments.getArgs();
    }

    @Override
    public boolean apply(SocketChannel socketChannel) throws IOException {
        int fileNameLength;
        String filename;
        if (socketChannel == null) {
            return false;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        filename = args[0];
        fileNameLength = filename.length();
        byteBuffer.put(command.getNumberOfCommand());
        byteBuffer.putInt(fileNameLength);
        byteBuffer.put(filename.getBytes());
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            socketChannel.write(byteBuffer);
        }
        return true;
    }
}
