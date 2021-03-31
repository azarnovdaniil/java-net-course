package ru.daniilazarnov;

import ru.daniilazarnov.serverOperations.FabricOfOperations;
import ru.daniilazarnov.serverOperations.ServerOperation;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;

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
//    private final ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(8192);
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
        welcomeMessage.put(Commands.pwd_or_string.getNumberOfCommand());
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
//        SocketChannel sc = (SocketChannel) key.channel();
        serverCommandInterpretation(key);
//        StringBuilder sb = new StringBuilder();
    }


    public void serverCommandInterpretation(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();
        byte numOfCommand = byteBuffer.get();
        Optional<Commands> command = Commands.getCommand(numOfCommand);
        ServerOperation serverOperation = FabricOfOperations.getOperation(command.get(), key);
        serverOperation.apply();
        }



//    private void sendMessageToClient(SelectionKey key, SocketChannel socketChannel, String message) throws IOException {
//        byteBuffer.clear();
//        byteBuffer.put((byte) 1);
//        byteBuffer.putInt(message.length());
//        byteBuffer.put(message.getBytes());
//        byteBuffer.flip();
//        socketChannel.write(byteBuffer);
//    }




    public static void main(String[] args) throws IOException {
        ServerConnection serverConnection = new ServerConnection();
        serverConnection.start();
    }
}
