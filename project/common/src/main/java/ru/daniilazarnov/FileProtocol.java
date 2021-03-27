package ru.daniilazarnov;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class FileProtocol {
    private static final int BYTE_ARRAY_SIZE = 1024;
    private static final ByteBuffer serviceBuffer = ByteBuffer.allocate(8192);
    private static final int DEFAULT_BYTE_BUFFER_SIZE = 8192;
    private static Path rootDir;
    private static SocketChannel clientSocketChannel;
    private static SelectionKey key;
    private static String serverHost;
    private static int serverPort;
    private static ByteBuffer byteBuffer;
    private static Selector selector;
//    private static ServerSocketChannel clientServerSocketChannel;

    public static SocketChannel getClientSocketChannel() {
        return clientSocketChannel;
    }


    //Можно ли как-то указать на клиенте сокет сервера, чтобы ожидать с него сообщения?
    public static void connect(InetSocketAddress inetSocketAddress) throws IOException {
//        selector = Selector.open();
//        clientSocketChannel = SocketChannel.open();
//        clientSocketChannel.bind(inetSocketAddress);
//        clientSocketChannel.register(selector, SelectionKey.OP_READ, SelectionKey.OP_WRITE);

        clientSocketChannel = SocketChannel.open(inetSocketAddress);
        clientSocketChannel.configureBlocking(false);
        selector = Selector.open();
        clientSocketChannel.register(selector, SelectionKey.OP_READ);
        handleReadFromServer();
    }

    public static void setRootDir(String dir) {
        rootDir = Paths.get(dir);
    }


    public static void setClientSocketChannel(SocketChannel sc) {
        clientSocketChannel = sc;
    }


    public static void receiveFile(SelectionKey key, SocketChannel socketChannel, Path path) throws IOException {
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
        /////////////////////////////////
        Path pathFile = Paths.get(path + File.separator + fileName);
        Files.write(pathFile, byteBuffer.array());
    }


    public static void commandInterpretationFromClient(String message) throws IOException {
        String[] splitMessage = message.split(" ");
        splitMessage[0] = splitMessage[0].toLowerCase();
        int fileNameLength;
        String filename;

        switch (splitMessage[0]) {
            case "mkd":
                if (clientSocketChannel == null) {
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
                clientSocketChannel.write(byteBuffer);
                break;
            case "connect":
                if (splitMessage.length != 3) {
                    System.out.println("Wrong command");
                    return;
                }
                serverHost = splitMessage[1];
                serverHost = splitMessage[2];
                clientSocketChannel = SocketChannel.open(new InetSocketAddress(serverHost, serverPort));
                clientSocketChannel.configureBlocking(false);
                selector = Selector.open();
                clientSocketChannel.register(selector, SelectionKey.OP_READ);
                handleReadFromServer();
                break;
            case "stor":
                if (clientSocketChannel == null) {
                    return;
                }
                if (splitMessage.length != 2) {
                    System.out.println("Wrong command");
                    return;
                }
                byteBuffer = ByteBuffer.allocate(DEFAULT_BYTE_BUFFER_SIZE);
                Path pathSrcFile = Paths.get(splitMessage[1]);
                if (!Files.exists(pathSrcFile)) {
                    return;
                }
                byteBuffer.put((byte) 1);
                filename = pathSrcFile.getFileName().toString();
                fileNameLength = filename.length();
                byteBuffer.putInt(fileNameLength);
                byteBuffer.put(filename.getBytes());
                byteBuffer.putInt((int) Files.size(pathSrcFile));
                FileChannel srcFileChannel = (FileChannel) Files.newByteChannel(pathSrcFile);
                while (srcFileChannel.read(byteBuffer) != 0) {
                    byteBuffer.flip();
                    clientSocketChannel.write(byteBuffer);
                    byteBuffer.clear();
                }
                srcFileChannel.close();
                break;
            case "retr":
                //передача с сервера на клиент
                if (clientSocketChannel == null) {
                    return;
                }
                if (splitMessage.length != 2) {
                    System.out.println("Wrong command");
                    return;
                }
                byteBuffer = ByteBuffer.allocate(DEFAULT_BYTE_BUFFER_SIZE);
                filename = splitMessage[1];
                fileNameLength = filename.length();
                byteBuffer.put((byte) 4);
                byteBuffer.putInt(fileNameLength);
                byteBuffer.put(filename.getBytes());
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    clientSocketChannel.write(byteBuffer);
                }
                break;
        }
    }


    public static void serverCommandInterpretation(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();
        byte command = byteBuffer.get();
        byteBuffer.compact();
        switch (command) {
            case 1:
                receiveFile(key, socketChannel, ((UserInfo) key.attachment()).getCurrentPath());
                break;
            case 2:
                makeDir(key, socketChannel);
                break;
            case 3:
//                changeDir();
                break;
            case 4:
                sendToClient(key, socketChannel);
                break;
        }
    }

    private static void sendToClient(SelectionKey key, SocketChannel socketChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BYTE_BUFFER_SIZE);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();
        int fileNameLength = byteBuffer.getInt();
        byteBuffer.compact();
        StringBuilder sb = new StringBuilder();
        while (fileNameLength > 0) {
            socketChannel.read(byteBuffer);
            byteBuffer.flip();
            while (byteBuffer.hasRemaining() && fileNameLength > 0) {
                sb.append((char) byteBuffer.get());
                fileNameLength--;
            }
        }
        String fileName = sb.toString();

        UserInfo userInfo = (UserInfo) key.attachment();
        Path currentDir = userInfo.getCurrentPath();
        Path targetFilePath = currentDir.resolve(Paths.get(fileName));
        if (!Files.exists(targetFilePath)) {
            return;
        }

        //Отправляем тклиенту текущее его положение в каталоге
        byteBuffer.clear();
        //длина строки пути
        byteBuffer.put((byte) 1);
        byteBuffer.putInt(targetFilePath.toString().length());
        byteBuffer.put(targetFilePath.toString().getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);


        //отправляем сам файл
        byteBuffer.clear();
        byteBuffer.put((byte) 2);

        String filename = targetFilePath.getFileName().toString();
        fileNameLength = filename.length();
        byteBuffer.putInt(fileNameLength);
        byteBuffer.put(filename.getBytes());
        byteBuffer.putInt((int) Files.size(targetFilePath));
        FileChannel srcFileChannel = (FileChannel) Files.newByteChannel(targetFilePath);
        while (srcFileChannel.read(byteBuffer) != 0) {
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        srcFileChannel.close();



        FileChannel fileChannel = (FileChannel) Files.newByteChannel(targetFilePath);
        while (fileChannel.read(byteBuffer) != 0) {
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }
    }


    private static void makeDir(SelectionKey key, SocketChannel socketChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BYTE_BUFFER_SIZE);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();
        int lengthDir = byteBuffer.getInt();
        byteBuffer.compact();
        StringBuilder sb = new StringBuilder();
        while (lengthDir > 0) {
            socketChannel.read(byteBuffer);
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                sb.append((char) byteBuffer.get());
                lengthDir--;
            }
        }
        Path beginUserDir = rootDir.resolve(((UserInfo) key.attachment()).getUserRoot());
        Path targetPath = Paths.get(sb.toString());

//        if (!targetPath.getRoot().toString().equals(beginUserDir.getFileName().toString())) {
//            return;
//        }
        targetPath = beginUserDir.resolve(targetPath);
        if (Files.exists(targetPath)) {
            return;
        }
        Files.createDirectories(targetPath);
    }


    /////////////////////////
//    нижележащие методы не готоовы
    public static void receiveBytesFromServer(SelectionKey key) throws IOException {
        byteBuffer = ByteBuffer.allocate(1);
        clientSocketChannel.read(byteBuffer);
        byteBuffer.flip();
        byte commandType = byteBuffer.get();
        switch (commandType) {
            case 1:
                receiveCurrentDirFromServer(key);
                break;
            case 2:
                receiveFile(key, clientSocketChannel, Paths.get("project/client/src/main/resources"));

        }
    }

    private static void receiveCurrentDirFromServer(SelectionKey key) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        clientSocketChannel.read(byteBuffer);
        byteBuffer.flip();
        int lengthDir = byteBuffer.getInt();
        byteBuffer = ByteBuffer.allocate(lengthDir);
//        byteBuffer.compact();
        clientSocketChannel.read(byteBuffer);
        byteBuffer.flip();
        StringBuilder sb = new StringBuilder();
        while (lengthDir > 0) {
            while (byteBuffer.hasRemaining()) {
                sb.append((char) byteBuffer.get());
                lengthDir--;
            }
        }
        System.out.println(sb.toString());
    }

    private static void handleReadFromServer() {
        new Thread(() -> {
            try {
                SelectionKey key = null;
                while (clientSocketChannel.isOpen()) {
                    selector.select();
                    Iterator<SelectionKey> iterator = selector.keys().iterator();
                    while (iterator.hasNext()) {
                        key = iterator.next();
                        if (key.isReadable()) {
                            receiveBytesFromServer(key);
                        }
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
