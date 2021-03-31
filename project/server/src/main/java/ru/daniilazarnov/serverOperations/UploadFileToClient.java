package ru.daniilazarnov.serverOperations;

import ru.daniilazarnov.Commands;
import ru.daniilazarnov.Protocol;
import ru.daniilazarnov.UserInfo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadFileToClient implements ServerOperation{
    SelectionKey key;
    SocketChannel socketChannel;
    public UploadFileToClient(SelectionKey key) {
        this.key = key;
        this.socketChannel = (SocketChannel) key.channel();
    }


    @Override
    public boolean apply() throws IOException {
//        ByteBuffer byteBuffer = ByteBuffer.allocate(8192);
        SocketChannel socketChannel = (SocketChannel) key.channel();
        String fileName = Protocol.getStringFromSocketChannel(socketChannel);
        Path currentDir = ((UserInfo) key.attachment()).getCurrentPath();
        Path targetFilePath = currentDir.resolve(Paths.get(fileName));
        Protocol.sendFileToSocketChannel(targetFilePath, socketChannel);
//        if (!Files.exists(targetFilePath)) {
//            return false;
//        }
//        byteBuffer.clear();
//        byteBuffer.put((Commands.retr.getNumberOfCommand()));
//        int fileNameLength = fileName.length();
//        byteBuffer.putInt(fileNameLength);
//        byteBuffer.put(fileName.getBytes());
//        byteBuffer.putInt((int) Files.size(targetFilePath));
//        FileChannel srcFileChannel = (FileChannel) Files.newByteChannel(targetFilePath);
//        while (srcFileChannel.read(byteBuffer) != 0) {
//            byteBuffer.flip();
//            socketChannel.write(byteBuffer);
//            byteBuffer.clear();
//        }
//        srcFileChannel.close();
        return true;
    }
}
