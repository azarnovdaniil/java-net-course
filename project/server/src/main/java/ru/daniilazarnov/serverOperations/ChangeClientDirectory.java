package ru.daniilazarnov.serverOperations;

import ru.daniilazarnov.Protocol;
import ru.daniilazarnov.UserInfo;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ChangeClientDirectory implements ServerOperation {
    private SelectionKey key;
    private SocketChannel socketChannel;
    public ChangeClientDirectory(SelectionKey key) {
        this.key = key;
        this.socketChannel = (SocketChannel) key.channel();
    }


    @Override
    public boolean apply() throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        UserInfo userInfo = (UserInfo) key.attachment();
        String targetP = Protocol.getStringFromSocketChannel(socketChannel);
        Path targetPath = Paths.get(targetP);
        Path currDir = userInfo.getCurrentPath();
        Path newPath = currDir.resolve(targetPath);
        if (!Files.exists(newPath)) {
            return false;
        }
        newPath = newPath.normalize();
        ((UserInfo) key.attachment()).setCurrentPath(newPath);

        Path currentPathForClient = userInfo.getUserRoot().getParent().relativize(newPath).normalize();
        Protocol.sendStringToSocketChannel(currentPathForClient.toString() + File.separator, socketChannel);
        return true;
    }
}
