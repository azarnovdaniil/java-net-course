package ru.daniilazarnov.serverOperations;

import ru.daniilazarnov.Protocol;
import ru.daniilazarnov.UserInfo;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MakeDirectory implements ServerOperation {
    private SelectionKey key;
    private SocketChannel socketChannel;
    public MakeDirectory(SelectionKey key) {
        this.key = key;
        this.socketChannel = (SocketChannel) key.channel();
    }


    @Override
    public boolean apply() throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        String dirName = Protocol.getStringFromSocketChannel(socketChannel);
        UserInfo userInfo = (UserInfo) key.attachment();
        Path userCurrentPath = userInfo.getCurrentPath();
        Path targetPath = Paths.get(dirName);

        targetPath = userCurrentPath.resolve(targetPath);
        if (Files.exists(targetPath)) {
            return false;
        }
        try {
            Files.createDirectories(targetPath);
            Protocol.sendStringToSocketChannel("Directory " + dirName + " created", socketChannel);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
