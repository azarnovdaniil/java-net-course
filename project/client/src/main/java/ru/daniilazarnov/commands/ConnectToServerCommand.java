package ru.daniilazarnov.commands;

import ru.daniilazarnov.ClientConnection;
import ru.daniilazarnov.Commands;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ConnectToServerCommand implements ICommand {
    private String[] args;
    private Commands command = Commands.user;

    public ConnectToServerCommand(ArgumentsForCommand arguments) {
        this.args = arguments.getArgs();
    }

    @Override
    public boolean apply(ClientConnection connection) throws IOException {
//        SocketChannel socketChannel = connection.getClientSocketChannel();
        if (args.length != 2) {
            System.out.println("Wrong command");
            return false;
        }
        String serverHost = args[0];
        int serverPort = Integer.parseInt(args[1]);
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(serverHost, serverPort));
        socketChannel.configureBlocking(false);
        Selector selector = connection.getSelector();
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);
        connection.handleReadFromServer();
        return true;
    }
}
