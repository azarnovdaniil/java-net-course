package ru.daniilazarnov.commands;

import ru.daniilazarnov.ClientConnection;
import ru.daniilazarnov.Commands;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class AuthCommand implements ICommands{
    private String[] args;
    Commands command = Commands.user;

    public AuthCommand(ArgumentsForCommand arguments) {
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
        byteBuffer.put(command.getNumberOfCommand());
        byteBuffer.putInt(args[0].length());
        byteBuffer.put(args[0].getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        return true;
    }
}
