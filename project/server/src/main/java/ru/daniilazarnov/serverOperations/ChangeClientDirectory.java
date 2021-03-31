package ru.daniilazarnov.serverOperations;

import ru.daniilazarnov.Protocol;
import ru.daniilazarnov.UserInfo;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ChangeClientDirectory implements ServerOperation{
    SelectionKey key;
    SocketChannel socketChannel;
    public ChangeClientDirectory(SelectionKey key) {
        this.key = key;
        this.socketChannel = (SocketChannel) key.channel();
    }


    @Override
    public boolean apply() throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        String targetP = Protocol.getStringFromSocketChannel(socketChannel);
        Path targetPath = Paths.get(targetP);
        Path currDir = ((UserInfo) key.attachment()).getCurrentPath();
        Path newPath = currDir.resolve(targetPath);
        if(!Files.exists(newPath)) {
            return false;
        }
        newPath = newPath.normalize();
        ((UserInfo) key.attachment()).setCurrentPath(newPath);
        return true;
    }
}
