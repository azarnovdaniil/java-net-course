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
import java.nio.file.Paths;

public class SaveFile implements ServerOperation {
    private final SelectionKey key;
    public SaveFile(SelectionKey key) {
        this.key = key;
    }


    @Override
    public boolean apply() throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        Path path = ((UserInfo) key.attachment()).getCurrentPath();
        String fileName = Protocol.getStringFromSocketChannel(socketChannel);

        ByteBuffer fileBuffer = Protocol.getFileInByteBufferFromSocketChannel(key);
        Path pathFile = Paths.get(path + File.separator + fileName);
        try {
            Files.write(pathFile, fileBuffer.array());
            ByteBuffer byteBuffer = Protocol.wrapStringInByteBuffer("File uploaded successfully");
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
