package ru.daniilazarnov;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server implements Runnable {

    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private final ByteBuffer buf = ByteBuffer.allocate(256);
    private int acceptedClientIndex = 1;
    private final ByteBuffer welcomeBuf = ByteBuffer.wrap("Добро пожаловать в чат!\n".getBytes());

    Server() throws IOException {
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.socket().bind(new InetSocketAddress(9999));
        this.serverSocketChannel.configureBlocking(false);
        this.selector = Selector.open();
        this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public static void main(String[] args) throws IOException {
        new Thread(new Server()).start();
    }

    @Override
    public void run() {
        try {
            System.out.println("Сервер запущен (Порт: 9999)");
            Iterator<SelectionKey> iter;
            SelectionKey key;
            while (this.serverSocketChannel.isOpen()) {
                selector.select();
                iter = this.selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    key = iter.next();
                    iter.remove();
                    if (key.isAcceptable()) {       // если работает селектор передачи данных
System.out.println("передаём");
                        handleAccept(key);
                    }
                    if (key.isReadable()) {         // если работает селектор чтения данных
System.out.println("получаем");
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
        String clientName = "Клиент #" + acceptedClientIndex;
        acceptedClientIndex++;
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ, clientName);
        System.out.println("Key"+key.toString());
        sc.write(welcomeBuf);
        welcomeBuf.rewind();
        System.out.println("Подключился новый клиент " + clientName);
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel ch = (SocketChannel) key.channel();
        StringBuilder sb = new StringBuilder();

        buf.clear();
        int read = 0;
        while ((read = ch.read(buf)) > 0) {
            buf.flip();
            byte[] bytes = new byte[buf.limit()];
            buf.get(bytes);
            sb.append(new String(bytes));
            buf.clear();
        }
 System.out.println("1");
        String msg;
        if (read < 0) {
            msg = key.attachment() + " покинул чат\n";
            acceptedClientIndex--;
            ch.close();
        } else {
            msg = key.attachment() + ": " + sb.toString();
        }

System.out.println("2");
System.out.println("Key"+key.toString());

 System.out.println(msg.toString());
        broadcastMessage(msg);
    }

    private void broadcastMessage(String msg) throws IOException {
        ByteBuffer msgBuf = ByteBuffer.wrap(msg.getBytes());
        for (SelectionKey key : selector.keys()) {
 System.out.println("key selector = " + key);
 System.out.println("key selector = " + key);
 System.out.println("key selector = " + key);
            if (key.isValid() && key.channel() instanceof SocketChannel) {
                SocketChannel sch = (SocketChannel) key.channel();
                sch.write(msgBuf);
                msgBuf.rewind();
            }
        }
    }

}


