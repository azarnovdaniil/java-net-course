package ru.gb.putilin.cloudstorage.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.EnumSet;

public class StorageServer {

    private static final Path STORAGE_ROOT = Path.of("C:\\Projects\\cloud");

    private final ServerSocketChannel serverSocketChannel;
    private ByteBuffer readBuffer;
    private ByteBuffer writeBuffer;


    private SocketChannel clientSocketChannel;

    public StorageServer(int port) throws IOException {
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.socket().bind(new InetSocketAddress(port));
        readBuffer = ByteBuffer.allocate(1024);
        writeBuffer = ByteBuffer.allocate(1);
    }

    public void run() throws IOException {
        System.out.println("Server is started!");
        clientSocketChannel = serverSocketChannel.accept();
        System.out.println("Client is connected!");
        Path path = readPath(clientSocketChannel);
        sendOkResponse(clientSocketChannel);
        readFile(clientSocketChannel, path);
    }

    private void readFile(SocketChannel socketChannel, Path path) throws IOException {

        FileChannel fileChannel = FileChannel.open(path,
                EnumSet.of(StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE));


        while (socketChannel.read(readBuffer) > 0) {
            readBuffer.flip();
            fileChannel.write(readBuffer);
            readBuffer.clear();
            System.out.println("Part of the File is received");
        }

        fileChannel.close();
        System.out.println("File is received!");
        socketChannel.close();
    }

    private Path readPath(SocketChannel socketChannel) throws IOException {
        String pathString;
        int read = socketChannel.read(readBuffer);
        readBuffer.flip();
        if (read == -1)
            throw new IOException("Client channel is closed!");
        pathString = (new String(readBuffer.array(), StandardCharsets.UTF_8)).substring(0, read);
        readBuffer.clear();
        Path path = Path.of(pathString);
        return Paths.get(STORAGE_ROOT.toString(), path.getFileName().toString());
    }

    private void sendOkResponse(SocketChannel socketChannel) throws IOException {
        writeBuffer.put(Byte.MAX_VALUE);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        writeBuffer.clear();
    }
}
