package ru.daniilazarnov.serverOperations;

import ru.daniilazarnov.Protocol;
import ru.daniilazarnov.UserInfo;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListOfFilesOrDirectories implements ServerOperation {
    private final SelectionKey key;
    public ListOfFilesOrDirectories(SelectionKey key) {
        this.key = key;
    }


    @Override
    public boolean apply() throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        UserInfo userInfo = (UserInfo) key.attachment();
        Path currentPath = userInfo.getCurrentPath();
        Stream<Path> files = Files.walk(currentPath);
        StringBuilder sb = new StringBuilder();
        List<Path> listOfPaths = files.collect(Collectors.toList());
        for (Path path : listOfPaths) {
            sb.append(path.getFileName()).append("\t");
        }
        ByteBuffer byteBuffer = Protocol.wrapStringAndCommandInByteBuffer(sb.toString());
        socketChannel.write(byteBuffer);
        return true;
    }
}
