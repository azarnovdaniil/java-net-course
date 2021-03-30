package ru.daniilazarnov;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class ServerConnection {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private String serverHost;
    private int serverPort;
    private Path pathToStorage;
    private static int acceptClientIndex;

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8199;
    private final ByteBuffer welcomeMessage = ByteBuffer.allocate(512);
    private final ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(8192);
    private static final Path DEFAULT_PATH_TO_STORAGE = Paths.get("project/server/src/main/resources");


    public ServerConnection(String serverHost, int serverPort, Path pathToStorage) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.pathToStorage = pathToStorage;
    }

    public ServerConnection(String serverHost, int serverPort) {
        this(serverHost, serverPort, DEFAULT_PATH_TO_STORAGE);
    }

    public ServerConnection() {
        this(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_PATH_TO_STORAGE);
    }

    public void start() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(serverHost, serverPort));
        serverSocketChannel.configureBlocking(false);
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        if (!Files.exists(pathToStorage)) {
            Files.createDirectory(pathToStorage);
        }
        welcomeMessage.put((byte) 1);
        welcomeMessage.putInt("Connection established".length());
        welcomeMessage.put("Connection established".getBytes());
        welcomeMessage.flip();
        waitConnections();
    }


    public void waitConnections() {
        try {
            System.out.println("Server запущен (Порт: 8199)");
            Iterator<SelectionKey> iterKey;
            SelectionKey key;
            while (this.serverSocketChannel.isOpen()) {
                selector.select();
                iterKey = this.selector.selectedKeys().iterator();
                while (iterKey.hasNext()) {
                    key = iterKey.next();
                    iterKey.remove();
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    }
                    if (key.isReadable()) {
                        handleRead(key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
        sc.configureBlocking(false);
        String userName = "user" + acceptClientIndex++;
        UserInfo info = new UserInfo(userName);
        Path beginDir = Paths.get("project/server/src/main/resources" + File.separator + "tempStorage");
        if (!Files.exists(beginDir)) {
            Files.createDirectory(beginDir);
        }
        info.setRootPath(beginDir);
        info.setCurrentPath(beginDir);
        sc.register(this.selector, SelectionKey.OP_READ, info);

        sc.write(welcomeMessage);
        welcomeMessage.flip();
        System.out.println("Подключился новый клиент");
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        serverCommandInterpretation(key);
        StringBuilder sb = new StringBuilder();
    }


    public void serverCommandInterpretation(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();
        byte command = byteBuffer.get();
        byteBuffer.compact();
        switch (command) {
            case Protocol.STOR:
                receiveFile(key, socketChannel, ((UserInfo) key.attachment()).getCurrentPath());
                sendMessageToClient(key, socketChannel,"File uploaded successfully");
                break;
            case Protocol.MKD:
                makeDir(key, socketChannel);
                break;
            case Protocol.CD:
                changeClientDirOnServer(key, socketChannel);
                break;
            case Protocol.RETR:
                sendToClient(key, socketChannel);
                break;
            case Protocol.USER:
                authorizationOnServer(key, socketChannel);
            default:
                ////////////////////
                //доработать
                return;
        }
    }

    private void changeClientDirOnServer(SelectionKey key, SocketChannel socketChannel) throws IOException {
        String targetP = Protocol.getStringFromSocketChannel(socketChannel);
        Path targetPath = Paths.get(targetP);
        Path currDir = ((UserInfo) key.attachment()).getCurrentPath();
        Path newPath = currDir.resolve(targetPath);
        if(!Files.exists(newPath)) {
            sendMessageToClient(key, socketChannel, "Wrong directory");
            return;
        }
        newPath = newPath.normalize();
        ((UserInfo) key.attachment()).setCurrentPath(newPath);
        sendMessageToClient(key, socketChannel,  "Current directory: " + newPath);
    }

    public void receiveFile(SelectionKey key, SocketChannel socketChannel, Path path) throws IOException {
        String fileName = Protocol.getStringFromSocketChannel(socketChannel);
        byteBuffer.clear();
        socketChannel.read(byteBuffer);
        byteBuffer.flip();
        int sizeFile = byteBuffer.getInt();
        ByteBuffer fileBuffer = ByteBuffer.allocate(sizeFile);
//                   Собираем файл из буфера
        while (sizeFile > 0) {
            while (byteBuffer.hasRemaining() && sizeFile > 0) {
                fileBuffer.put(byteBuffer.get());
                sizeFile--;
            }
            byteBuffer.clear();
            socketChannel.read(byteBuffer);
            byteBuffer.flip();
        }
        Path pathFile = Paths.get(path + File.separator + fileName);
        try {
            Files.write(pathFile, fileBuffer.array());
        } catch (IOException e) {
            sendMessageToClient(key, socketChannel, "File transfer error");
        }
    }

    private void makeDir(SelectionKey key, SocketChannel socketChannel) throws IOException {
        String dirName = Protocol.getStringFromSocketChannel(socketChannel);
        Path userCurrentPath = ((UserInfo) key.attachment()).getCurrentPath();
        Path targetPath = Paths.get(dirName);

        targetPath = userCurrentPath.resolve(targetPath);
        if (Files.exists(targetPath)) {
            sendMessageToClient(key, socketChannel, "The directory already exists");
            return;
        }
        try {
            Files.createDirectories(targetPath);
            sendMessageToClient(key, socketChannel, "The directory was created successfully");
        } catch (IOException e) {
            sendMessageToClient(key, socketChannel, "Unable to create a directory");
        }
        System.out.println(Files.exists(targetPath));
    }

    private void sendToClient(SelectionKey key, SocketChannel socketChannel) throws IOException {
//        String fileName = getStringFromBytes(socketChannel);
        String fileName = Protocol.getStringFromSocketChannel(socketChannel);
        Path currentDir = getCurrentDir(key, socketChannel);
        Path targetFilePath = currentDir.resolve(Paths.get(fileName));
        if (!Files.exists(targetFilePath)) {
            return;
        }
        byteBuffer.clear();
        byteBuffer.put((byte) 2);
        int fileNameLength = fileName.length();
        byteBuffer.putInt(fileNameLength);
        byteBuffer.put(fileName.getBytes());
        byteBuffer.putInt((int) Files.size(targetFilePath));
        FileChannel srcFileChannel = (FileChannel) Files.newByteChannel(targetFilePath);
        while (srcFileChannel.read(byteBuffer) != 0) {
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        srcFileChannel.close();
    }

    private Path getCurrentDir(SelectionKey key, SocketChannel socketChannel) {
        UserInfo userInfo = (UserInfo) key.attachment();
        return userInfo.getCurrentPath();
    }

    private void sendMessageToClient(SelectionKey key, SocketChannel socketChannel, String message) throws IOException {
        byteBuffer.clear();
        byteBuffer.put((byte) 1);
        byteBuffer.putInt(message.length());
        byteBuffer.put(message.getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
    }

    private void authorizationOnServer(SelectionKey key, SocketChannel socketChannel) throws IOException {
        String userName = Protocol.getStringFromSocketChannel(socketChannel);
        ((UserInfo) key.attachment()).setName(userName);
        Path path = ((UserInfo) key.attachment()).getCurrentPath().getParent().resolve(Paths.get(userName));
        ((UserInfo) key.attachment()).setRootPath(path);
        ((UserInfo) key.attachment()).setCurrentPath(path);
        if(!Files.exists(path)) {
            Files.createDirectory(path);
        }
        String message = "You are logged in as " + ((UserInfo) key.attachment()).getName() +
                "\nCurrent directory " + path.getFileName() + File.separator;
        sendMessageToClient(key,  socketChannel, message);
    }



    public static void main(String[] args) throws IOException {
        ServerConnection serverConnection = new ServerConnection();
        serverConnection.start();
    }
}
