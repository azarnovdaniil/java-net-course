package ru.daniilazarnov;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.nio.file.Path;


public class Network {
    private static final String HOST = "localhost";
    private static final int PORT = 8888;
    private SocketChannel channel;

    public void start() {
        Thread t = new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) {
                                socketChannel.pipeline().addLast(new ClientHandler());
                                channel = socketChannel;
                            }
                        });
                ChannelFuture future = b.connect(HOST, PORT).sync();
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        });
        t.start();
    }


    public void sendFile(String file) {
        try {
            FileSender.sendFile(Path.of(file), channel, ChannelFutureListen.getChannelFutureListener("File is successfully received\n"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendStringAndCommand(String fileName, byte command) {
        ReceiveString.sendString(fileName, channel, command,
                ChannelFutureListen.getChannelFutureListener("\nName of file received"));
    }
}
