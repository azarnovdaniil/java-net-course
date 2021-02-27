package ru.daniilazarnov.actual;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

public class Network {
    private Channel channel;
    private Scanner scanner;
    private RequestController controller;
    private static final String HOST = "localhost";
    private static final int PORT = 8189;

    public static void main(String[] args) {
        new Network();
    }

    public Network() {
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
                                .addLast(new ClientHandler());
                            }
                        });
                ChannelFuture future = b.connect(HOST, PORT).sync();

                controller = new RequestController(channel);

                scanner = new Scanner(System.in);
                while (scanner.hasNext()) {
                    String str = scanner.nextLine();
                    controller.command(str);
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
