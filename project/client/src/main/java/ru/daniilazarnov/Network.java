package ru.daniilazarnov;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class Network {
    private SocketChannel channel;

    private static final String HOST = "localhost";
    private static final int PORT = 8189;

    public Network() {
        Thread t = new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) {
                                channel = socketChannel;
                                socketChannel.pipeline().addLast(new ClientHandler());
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

    public void close() {
        channel.close();
        System.out.println("Connection interrupted");
    }

    public void sendFile(String file) {
        try {
            FileSender.setLoadingStatus(true);
            FileSender.sendFile(Path.of(file), channel, getChannelFutureListenerSendFile("Файл успешно передан\n"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnect() {
        if (channel != null)
            return channel.isActive();
        else return false;
    }

    private ChannelFutureListener getChannelFutureListenerSendFile(String s) {
        return future -> {
            if (!future.isSuccess()) {
                System.err.println(s + "не был");
            }
            if (future.isSuccess()) {
                System.out.print(s);
                FileSender.setLoadingStatus(false);

            }
        };
    }
}
