package ru.daniilazarnov;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server implements Runnable{

    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private int acceptedClientIndex = 1;
    private final ByteBuffer welcomeBuf = ByteBuffer.wrap("Добро пожаловать в чат!\n".getBytes());



    public static void main(String[] args) {

    }

    public Server() throws IOException {
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.socket().bind(new InetSocketAddress(8189));
        this.serverSocketChannel.configureBlocking(false);
        this.selector = Selector.open();
        this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        try {
            System.out.println("Сервер запущен! (Порт 8189)");
            Iterator<SelectionKey> iter;
            SelectionKey key;
            while (this.serverSocketChannel.isOpen()) {
                selector.select();
                iter = this.selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    key = iter.next();
                    iter.remove();
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    }
                    if (key.isReadable()) {
                        handleAccept(key);
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
        String clientName = "Клиент №" + acceptedClientIndex;
        acceptedClientIndex++;
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ, clientName);
        sc.write(welcomeBuf);
        welcomeBuf.rewind();
        System.out.println("Подключился новый клиент " + clientName);
    }
}
