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
    private SelectionKey key;
    private SocketChannel socketChannel;
    public SaveFile(SelectionKey key) {
        this.key = key;
        this.socketChannel = (SocketChannel) key.channel();
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
            Protocol.sendStringToSocketChannel("File uploaded successfully", socketChannel);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
