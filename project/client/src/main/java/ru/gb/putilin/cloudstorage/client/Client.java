package ru.gb.putilin.cloudstorage.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class Client {

    private SocketChannel clientSocketChannel;
    private ByteBuffer writeBuffer;
    private ByteBuffer readBuffer;

    public Client(int port) throws IOException {
        clientSocketChannel = createClient(port);
        writeBuffer = ByteBuffer.allocate(1024);
        readBuffer = ByteBuffer.allocate(1);
    }

    public void sendFile(String pathString) throws IOException {
        Path path = Path.of(pathString);
        FileChannel fileChannel = FileChannel.open(path);

        while (fileChannel.read(writeBuffer) > 0) {
            writeBuffer.flip();
            clientSocketChannel.write(writeBuffer);
            writeBuffer.clear();
            System.out.println("Part of the file is written to buffer!");
        }
    }

    public void sendUri(String pathString) throws IOException {
        writeBuffer.put(pathString.getBytes(StandardCharsets.UTF_8));
        writeBuffer.flip();
        clientSocketChannel.write(writeBuffer);
        writeBuffer.clear();
        System.out.println("Uri is sent!");

    }

    public boolean readResponse() throws IOException {
        int read = clientSocketChannel.read(readBuffer);
        readBuffer.flip();
        if (read == -1)
            throw new IOException("Server channel is closed!");
        boolean isOk = readBuffer.array()[0] == Byte.MAX_VALUE;
        readBuffer.clear();
        return isOk;
    }

    public void removeClient() throws IOException {
        clientSocketChannel.close();
    }

    private SocketChannel createClient(int port) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        SocketAddress socketAddress = new InetSocketAddress(port);
        socketChannel.connect(socketAddress);
        return socketChannel;
    }

}
