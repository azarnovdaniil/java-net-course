package ru.daniilazarnov;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import ru.daniilazarnov.newp.CommandController;

import java.util.Scanner;

public class Network {
    private Channel channel;
    private Scanner scanner;
    private CommandController controller;
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
                                        .addLast(new ObjectDecoder(1024 * 1024 * 1024, ClassResolvers.cacheDisabled(null)))
                                        .addLast(new ObjectEncoder())
                                        .addLast(new ClientHandler());
                            }
                        });
                ChannelFuture future = b.connect(HOST, PORT).sync();

                controller = new CommandController(channel);

                scanner = new Scanner(System.in);
                while (scanner.hasNext()) {
                    String str = scanner.nextLine();
                    String[] request = str.split("\\s", 3);
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
