package ru.daniilazarnov;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.util.Scanner;

public class Network {
    private SocketChannel socket;
    private static final String HOST = "localhost";
    private static final int PORT = 8189;
    private static Scanner scanner;


    public static void main(String[] args) {
        new Network();
    }

    public Network() {
        new Thread(() -> {

            scanner = new Scanner(System.in);

            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socket = socketChannel;
                                socketChannel.pipeline()
                                        .addLast(new StringDecoder())
                                        .addLast(new StringEncoder())
                                        .addLast(new ClientHandler());
                            }
                        });

                ChannelFuture future = b.connect(HOST, PORT).sync();

                while (scanner.hasNext()) {
                    String str = scanner.nextLine();
                    Channel channel = future.sync().channel();
                    channel.writeAndFlush(str);
                    channel.flush();
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
