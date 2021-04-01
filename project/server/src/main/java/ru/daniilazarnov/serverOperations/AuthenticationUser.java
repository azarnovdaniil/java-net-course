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

public class AuthenticationUser implements ServerOperation {
    private final SelectionKey key;
    public AuthenticationUser(SelectionKey key) {
        this.key = key;
    }

    @Override
    public boolean apply() throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        String userName = Protocol.getStringFromSocketChannel(socketChannel);
        ((UserInfo) key.attachment()).setName(userName);
        Path path = ((UserInfo) key.attachment()).getCurrentPath().getParent().resolve(Paths.get(userName));
        ((UserInfo) key.attachment()).setRootPath(path);
        ((UserInfo) key.attachment()).setCurrentPath(path);
        if (!Files.exists(path)) {
            Files.createDirectory(path);
            Protocol.sendStringToSocketChannel("Created a section for a new user " + userName, socketChannel);
            return false;
        }
        String message = "You are logged in as " + ((UserInfo) key.attachment()).getName()
                + "\nCurrent directory " + path.getFileName() + File.separator;
        Protocol.sendStringToSocketChannel(message, socketChannel);
        return true;
    }
}
