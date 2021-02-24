package ru.atoroschin;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class RealNettyClient {
    private static final int PART_SIZE = 10 * 1024 * 1024;
    private static final int SERVER_PORT = 8189;
    private static final String HOST = "localhost";
    private static final Logger LOGGER = Logger.getLogger("");
    private static final String LOG_PROP = "logclient" + File.separator + "logging.properties";

    public static void main(String[] args) throws InterruptedException {
        try {
            LogManager manager = LogManager.getLogManager();
            manager.readConfiguration(new FileInputStream(LOG_PROP));
        } catch (IOException e) {
            e.printStackTrace();
        }
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap clientBootstrap = new Bootstrap();
            final int countFour = 4;
            final int countFive = -5;
            clientBootstrap.group(group);
            clientBootstrap.channel(NioSocketChannel.class);
            clientBootstrap.remoteAddress(new InetSocketAddress(HOST, SERVER_PORT));
            clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) {
                    socketChannel.pipeline()
                            .addLast(new LengthFieldBasedFrameDecoder(ByteOrder.BIG_ENDIAN, PART_SIZE, 1,
                                    countFour, countFive, 0, true))
                            .addLast(new ClientAuthHandler())
                            .addLast(new ClientHandlerNetty());
                }
            });
            ChannelFuture channelFuture = clientBootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Не удается установить соединение", e);
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
