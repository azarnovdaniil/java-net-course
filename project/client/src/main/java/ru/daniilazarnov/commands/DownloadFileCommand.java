package ru.daniilazarnov.commands;

import ru.daniilazarnov.ClientConnection;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class DownloadFileCommand implements ICommands{
    private String[] args;
    byte NUMBER_OF_COMMAND = 4;

    public DownloadFileCommand(ArgumentsForCommand arguments) {
        this.args = arguments.getArgs();
    }

    @Override
    public boolean apply(ClientConnection connection) throws IOException {
        SocketChannel socketChannel = connection.getClientSocketChannel();
        int fileNameLength;
        String filename;
        if (socketChannel == null) {
            return false;
        }
        if (args.length != 1) {
            System.out.println("Wrong command");
            return false;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(8192);
        filename = args[0];
        fileNameLength = filename.length();
        byteBuffer.put(NUMBER_OF_COMMAND);
        byteBuffer.putInt(fileNameLength);
        byteBuffer.put(filename.getBytes());
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            socketChannel.write(byteBuffer);
        }
        return true;
    }
}
