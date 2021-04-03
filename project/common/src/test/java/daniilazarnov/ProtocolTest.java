package daniilazarnov;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.daniilazarnov.Protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ProtocolTest {
    static Selector selector;

    @BeforeEach
    public void prepareServer() {
        new Thread(() -> {
            try {
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.bind(new InetSocketAddress("localhost", 8200));
                serverSocketChannel.configureBlocking(false);
                selector = Selector.open();
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                Iterator<SelectionKey> iterator;
                while (serverSocketChannel.isOpen()) {
                    selector.select();
                    iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                            socketChannel.configureBlocking(false);

//                            Protocol.sendStringToSocketChannel("Test string", socketChannel);


//                            ByteBuffer byteBuffer = ByteBuffer.allocate(128);
//                            byteBuffer.putInt("Test string".length());
//                            byteBuffer.put("Test string".getBytes());
//                            byteBuffer.flip();
//                            socketChannel.write(byteBuffer);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }



    @Test
    public void TestStringFromSocketChannel() throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 8200));
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        while (socketChannel.isOpen()) {
            selector.select();
            Assertions.assertEquals("Test string", Protocol.getStringFromSocketChannel((SocketChannel) selector.selectedKeys().iterator().next().channel()));
            socketChannel.close();
        }
    }



}
