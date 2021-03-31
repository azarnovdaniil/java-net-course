package ru.daniilazarnov.commands;

import ru.daniilazarnov.ClientConnection;
import ru.daniilazarnov.Commands;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class PresentWorkDirectory implements ICommand {
    private String[] args;
    private Commands command = Commands.pwd;
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public PresentWorkDirectory(ArgumentsForCommand arguments) {
        this.args = arguments.getArgs();
    }

    @Override
    public boolean apply(ClientConnection connection) throws IOException {
        SocketChannel socketChannel = connection.getClientSocketChannel();
        if (socketChannel == null) {
            return false;
        }
        if (args.length != 0) {
            System.out.println("Wrong command");
            return false;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        byteBuffer.put(command.getNumberOfCommand());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        return true;
    }
}
