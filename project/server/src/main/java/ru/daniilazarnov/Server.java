package ru.daniilazarnov;

import ru.daniilazarnov.data.User;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.MessageFormat;
import java.util.Iterator;

public class Server implements Runnable{
    private final ServerSocketChannel socketChannel;
    private final Selector selector;
    private final ByteBuffer mainBuffer = ByteBuffer.allocate(256);
    private int clientIndex = 1;
    private final ByteBuffer welcomeBuffer = ByteBuffer.wrap("You have connected to the server".getBytes());

    public Server() throws IOException {
        this.socketChannel = ServerSocketChannel.open();
        this.socketChannel.socket().bind(new InetSocketAddress(8189));
        this.socketChannel.configureBlocking(false);
        this.selector = Selector.open();
        this.socketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public static void main(String[] args) throws IOException {
        new Thread(new Server()).start();
    }

    @Override
    public void run() {
        try {
            System.out.println("----- Server starting -----\n");
            Iterator<SelectionKey> iterator;
            SelectionKey key;
            while (this.socketChannel.isOpen()){
                selector.select();
                iterator = this.selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()){
                        handleAccept(key);
                    }
                    if (key.isReadable()){
                        handleRead(key);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleAccept(SelectionKey key) throws IOException{
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        User user = new User("User" + clientIndex, "1234");
        clientIndex++;
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ, user);
        socketChannel.write(welcomeBuffer);
        welcomeBuffer.rewind();
        System.out.println(String.format("%s connected", user.userName));
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel ch = (SocketChannel) key.channel();
        StringBuilder sb = new StringBuilder();

        int read = readMessage(ch, sb);

        if (sb.toString().toCharArray()[0] == 13 && sb.toString().toCharArray()[1] == 10){
            executeCommand(read, key, ch);
        } else {
            ((User) key.attachment()).command.append(sb);
        }
    }

    private int readMessage(SocketChannel ch, StringBuilder sb) throws IOException {
        mainBuffer.clear();
        int read = 0;
        while ((read = ch.read(mainBuffer)) > 0) {
            mainBuffer.flip();
            byte[] bytes = new byte[mainBuffer.limit()];
            mainBuffer.get(bytes);
            sb.append(new String(bytes));
            mainBuffer.clear();
        }
        return read;
    }

    private void executeCommand(int read,  SelectionKey key, SocketChannel ch) throws IOException {
//        String msg;
//        if (read < 0) {
//            ch.close();
//        } else {
//
//        }

    }
}
