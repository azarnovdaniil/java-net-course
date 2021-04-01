package ru.daniilazarnov.serverOperations;

import ru.daniilazarnov.UserInfo;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class DisconnectClient implements ServerOperation {
    private final SelectionKey key;
    public DisconnectClient(SelectionKey key) {
        this.key = key;
    }

    @Override
    public boolean apply() throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        socketChannel.close();
        System.out.println(((UserInfo) key.attachment()).getName() + " disconnected");
        return true;
    }
}
