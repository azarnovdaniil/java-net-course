package ru.daniilazarnov;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class NioClient {
    private ByteBuffer buffer = ByteBuffer.allocate (16);


    private void run() throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_CONNECT);
        channel.connect(new InetSocketAddress("localhost", 8189));
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(2);
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                try {
                    queue.put(line);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SelectionKey key = channel.keyFor(selector);
                key.interestOps(SelectionKey.OP_WRITE);
                selector.wakeup();
            }
        });
    while (true){
        selector.select();
        for (SelectionKey selectionKey : selector.selectedKeys()){
            if (selectionKey.isConnectable()){
                channel.finishConnect();
                selectionKey.interestOps(SelectionKey.OP_WRITE);
            } else if (selectionKey.isReadable()){
                buffer.clear();
                channel.read(buffer);
                System.out.println("Recieved = " + new String(buffer.array()));
            } else if (selectionKey.isWritable()){
                String line = queue.poll();
                if (line != null){
                    channel.write(ByteBuffer.wrap(line.getBytes()));
                }
                selectionKey.interestOps(SelectionKey.OP_READ);
            }
        }
    }

    }

    public static void main(String[] args) throws Exception{
        new NioClient().run();

    }

}
