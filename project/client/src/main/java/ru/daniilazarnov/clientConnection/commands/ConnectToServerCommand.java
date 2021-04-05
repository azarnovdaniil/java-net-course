package ru.daniilazarnov.clientConnection.commands;

import ru.daniilazarnov.Commands;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class ConnectToServerCommand implements ICommand {
    private final String[] args;
    private final Commands command = Commands.user;

    public ConnectToServerCommand(ArgumentsForCommand arguments) {
        this.args = arguments.getArgs();
    }

    @Override
    public boolean apply(SocketChannel socketChannel) throws IOException {
//        if (args.length != 2) {
//            System.out.println("Wrong command");
//            return false;
//        }
//        String serverHost = args[0];
//        int serverPort = Integer.parseInt(args[1]);
//        socketChannel = SocketChannel.open(new InetSocketAddress(serverHost, serverPort));
//        socketChannel.configureBlocking(false);
//        Selector selector = connection.getSelector();
//        selector = Selector.open();
//        socketChannel.register(selector, SelectionKey.OP_READ);
//        connection.handleReadFromServer();
        return true;
    }
}
