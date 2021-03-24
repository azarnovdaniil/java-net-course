package ru.daniilazarnov;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileProtocol {
    private static final int BYTE_ARRAY_SIZE = 1024;
    private static final ByteBuffer serviceBuffer = ByteBuffer.allocate(8192);
    private static Path rootDir;
    private static SocketChannel socketChannel;
    private static SelectionKey key;
    private static String serverHost;
    private static int serverPort;
    private static ByteBuffer byteBuffer;


    public static void connect(InetSocketAddress inetSocketAddress) throws IOException {
        socketChannel = SocketChannel.open(inetSocketAddress);
    }

    public void setRootDir(String dir) {
        rootDir = Paths.get(dir);
    }


    public static void setSocketChannel(SocketChannel sc) {
        socketChannel = sc;
    }


    public static void sendToServer(SelectionKey key, SocketChannel socketChannel) throws IOException {
        socketChannel.read(serviceBuffer);
        serviceBuffer.flip();
        int fileNameLength = serviceBuffer.getInt();
        serviceBuffer.compact();
        StringBuilder sb = new StringBuilder();
        while (fileNameLength > 0) {
            socketChannel.read(serviceBuffer);
            serviceBuffer.flip();
            while (serviceBuffer.hasRemaining() && fileNameLength > 0) {
                sb.append((char) serviceBuffer.get());
                fileNameLength--;
            }
            serviceBuffer.compact();
        }
        String fileName = sb.toString();

//        serviceBuffer.compact();
        socketChannel.read(serviceBuffer);
        serviceBuffer.flip();

        int sizeFile = serviceBuffer.getInt();
        ByteBuffer byteBuffer = ByteBuffer.allocate(sizeFile);


//                   Собираем файл из буфера
        while (sizeFile > 0) {
            while (serviceBuffer.hasRemaining() && byteBuffer.limit() != byteBuffer.position()) {
                byteBuffer.put(serviceBuffer.get());
                sizeFile--;
            }
            serviceBuffer.clear();
            socketChannel.read(serviceBuffer);
            serviceBuffer.flip();
        }
        Path pathFile = Paths.get(((userInfo) key.attachment()).getCurrentPath() + File.separator + fileName);
        Files.write(pathFile, byteBuffer.array());

//        Path path = (Path) key.attachment();
//        socketChannel.write(ByteBuffer.wrap(path.toString().getBytes(StandardCharsets.UTF_8)));



    }


    public static void trueSender(String message) throws IOException {
//        message = message.toLowerCase();
        String[] splitMessage = message.split(" ");
        splitMessage[0] = splitMessage[0].toLowerCase();
        switch (splitMessage[0]) {
            case "mkd":
                if (socketChannel == null) {
                    return;
                }
                if (splitMessage.length != 2) {
                    System.out.println("Wrong command");
                    return;
                }
                byteBuffer = ByteBuffer.allocate(5 + splitMessage[1].length());
                byteBuffer.put((byte) 2);
                byteBuffer.putInt(splitMessage[1].length());
                byteBuffer.put(splitMessage[1].getBytes());
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
                break;
            case "connect":
                if (splitMessage.length != 3) {
                    System.out.println("Wrong command");
                    return;
                }
                serverHost = splitMessage[1];
                serverHost = splitMessage[2];
                socketChannel = SocketChannel.open(new InetSocketAddress(serverHost, serverPort));
                break;
            case "stor":
                if (socketChannel == null) {
                    return;
                }
                if (splitMessage.length != 2) {
                    System.out.println("Wrong command");
                    return;
                }
                byteBuffer = ByteBuffer.allocate(8192);
                Path pathSrcFile = Paths.get(splitMessage[1]);
                if (!Files.exists(pathSrcFile)) {
                    return;
                }
                byteBuffer.put((byte) 1);
                String filename = pathSrcFile.getFileName().toString();
                int fileNameLength = filename.length();
                byteBuffer.putInt(fileNameLength);
                byteBuffer.put(filename.getBytes());
                byteBuffer.putInt((int) Files.size(pathSrcFile));
                FileChannel srcFileChannel = (FileChannel) Files.newByteChannel(pathSrcFile);
                while (srcFileChannel.read(byteBuffer) != 0) {
                    byteBuffer.flip();
                    socketChannel.write(byteBuffer);
                    byteBuffer.clear();
                }
                srcFileChannel.close();

        }
    }


    public static void trueReceiver(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();
        byte command = byteBuffer.get();
        byteBuffer.compact();
        switch (command) {
            case 1:
                sendToServer(key, socketChannel);
                break;
            case 2:
                makeDir(key, socketChannel);
                break;
            case 3:
//                changeDir();
                break;
            case 4:
//                sendToClient();
                break;
        }
    }

    private static void makeDir(SelectionKey key, SocketChannel socketChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8192);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();
        int lengthDir = byteBuffer.getInt();
        byteBuffer.compact();
        StringBuilder sb = new StringBuilder();
        while (lengthDir != 0) {
            socketChannel.read(byteBuffer);
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                sb.append((char) byteBuffer.get());
                lengthDir--;
            }
        }
        Path beginUserDir = rootDir.resolve(((userInfo) key.attachment()).getUserRoot());
        Path targetPath = Paths.get(sb.toString());

        if (!targetPath.getRoot().toString().equals(beginUserDir.getFileName().toString())) {
            return;
        }
        if (Files.exists(targetPath)) {
            return;
        }
        Files.createDirectories(targetPath);
    }
}
