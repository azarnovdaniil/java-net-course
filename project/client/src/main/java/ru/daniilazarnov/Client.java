package ru.daniilazarnov;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {


    public static void main(String[] args) {

        SocketChannel sc = null;

        try {
            sc = SocketChannel.open();
            sc.connect(new InetSocketAddress(8189));

            ByteBuffer buf = ByteBuffer.allocate(1024);
            Scanner console = new Scanner(System.in);

            while (true) {
                String str = console.nextLine();
                buf.put(str.getBytes(StandardCharsets.UTF_8));
                buf.flip();
                while (buf.hasRemaining()) {
                    sc.write(buf);
                }
                buf.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                sc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
