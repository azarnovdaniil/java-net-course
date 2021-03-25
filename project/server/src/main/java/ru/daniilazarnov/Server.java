package ru.daniilazarnov;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

class Main {
    public static void main(String[] args) throws IOException {
        new Thread(new Server()).start();
    }
}

public class Server implements Runnable {
    private static final int PORT_SERVER = 8199;
    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private final ByteBuffer buffer = ByteBuffer.allocate(512);
    private final ByteBuffer welcomeMessage = ByteBuffer.wrap("Соединение установлено".getBytes(StandardCharsets.UTF_8));

    public Server() throws IOException {
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.socket().bind(new InetSocketAddress(PORT_SERVER));
        this.serverSocketChannel.configureBlocking(false);
        this.selector = Selector.open();
        this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        FileProtocol.setRootDir("project/server/src/main/resources");
    }

    @Override
    public void run() {
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
        Path beginDir = Paths.get("project/server/src/main/resources");
        UserInfo info = new UserInfo("user1");
        info.setCurrentPath(beginDir);//временное решение для тестирования
        sc.register(this.selector, SelectionKey.OP_READ, info);

        sc.write(welcomeMessage);
        welcomeMessage.rewind();
        System.out.println("Подключился новый клиент");
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        FileProtocol.trueReceiver(key);
        StringBuilder sb = new StringBuilder();

        buffer.clear();
        while (sc.read(buffer) > 0) {
            buffer.flip();
        }
    }
}
