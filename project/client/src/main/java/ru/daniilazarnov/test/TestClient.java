package ru.daniilazarnov.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

public class TestClient {
    private Channel channel;
    private Scanner scanner;
    private TestCC controller;
    private static final String HOST = "localhost";
    private static final int PORT = 8189;

    public static void main(String[] args) {
        new TestClient();
    }

    public TestClient() {
        new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel;
                                socketChannel.pipeline()
                                .addLast(new TestClientHandler());
                            }
                        });
                ChannelFuture future = b.connect(HOST, PORT).sync();

                controller = new TestCC(channel);

                scanner = new Scanner(System.in);
                while (scanner.hasNext()) {
                    String str = scanner.nextLine();
                    String[] request = str.split("\\s", 2);
                    controller.command(request);
                }

                future.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        }).start();
    }
}
