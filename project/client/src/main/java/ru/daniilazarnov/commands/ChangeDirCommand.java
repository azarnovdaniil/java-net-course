package ru.daniilazarnov.commands;

import ru.daniilazarnov.ClientConnection;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ChangeDirCommand implements ICommands{
    private String[] args;
    byte NUMBER_OF_COMMAND = 3;

    public ChangeDirCommand(ArgumentsForCommand arguments) {
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
        ByteBuffer byteBuffer = ByteBuffer.allocate(8192);
        String targetDir = args[0];
        int targetDitLength = targetDir.length();
        byteBuffer.put(NUMBER_OF_COMMAND);
        byteBuffer.putInt(targetDitLength);
        byteBuffer.put(targetDir.getBytes());
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            socketChannel.write(byteBuffer);
        }
        return true;
    }
}