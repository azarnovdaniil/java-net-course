package ru.daniilazarnov.clientConnection.commands;

import ru.daniilazarnov.Commands;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ChangeDirCommand implements ICommand {
    private final String[] args;
    private final Commands command = Commands.cd;
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public ChangeDirCommand(ArgumentsForCommand arguments) {
        this.args = arguments.getArgs();
    }


    @Override
    public boolean apply(SocketChannel socketChannel) throws IOException {
        if (socketChannel == null) {
            return false;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        String targetDir = args[0];
        int targetDitLength = targetDir.length();
        byteBuffer.put(command.getNumberOfCommand());
        byteBuffer.putInt(targetDitLength);
        byteBuffer.put(targetDir.getBytes());
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            socketChannel.write(byteBuffer);
        }
        return true;
    }
}
