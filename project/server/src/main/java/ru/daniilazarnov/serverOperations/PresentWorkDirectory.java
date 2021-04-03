package ru.daniilazarnov.serverOperations;

import ru.daniilazarnov.Protocol;
import ru.daniilazarnov.UserInfo;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

public class PresentWorkDirectory implements ServerOperation {
    private final SelectionKey key;
    public PresentWorkDirectory(SelectionKey key) {
        this.key = key;
    }


    @Override
    public boolean apply() throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        UserInfo userInfo = (UserInfo) key.attachment();
        Path currentPath = userInfo.getCurrentPath();
        Path rootPath = userInfo.getUserRoot().getParent();
        Path currentPathForUser = rootPath.relativize(currentPath).normalize();
        ByteBuffer byteBuffer = Protocol.wrapStringAndCommandInByteBuffer(currentPathForUser.toString() + File.separator);
        socketChannel.write(byteBuffer);
        return true;
    }
}
