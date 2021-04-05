package ru.daniilazarnov.serverOperations;

import ru.daniilazarnov.Protocol;
import ru.daniilazarnov.UserInfo;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MakeDirectory implements ServerOperation {
    private final SelectionKey key;
    public MakeDirectory(SelectionKey key) {
        this.key = key;
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
            ByteBuffer byteBuffer = Protocol.wrapStringInByteBuffer("Directory " + dirName + " created");
            socketChannel.write(byteBuffer);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
