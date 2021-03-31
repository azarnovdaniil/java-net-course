package ru.daniilazarnov.serverOperations;
import ru.daniilazarnov.Protocol;
import ru.daniilazarnov.UserInfo;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadFileToClient implements ServerOperation {
    private SelectionKey key;
    private SocketChannel socketChannel;
    public UploadFileToClient(SelectionKey key) {
        this.key = key;
        this.socketChannel = (SocketChannel) key.channel();
    }


    @Override
    public boolean apply() throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        String fileName = Protocol.getStringFromSocketChannel(socketChannel);
        Path currentDir = ((UserInfo) key.attachment()).getCurrentPath();
        Path targetFilePath = currentDir.resolve(Paths.get(fileName));
        Protocol.sendFileToSocketChannel(targetFilePath, socketChannel);
        return true;
    }
}
