package ru.daniilazarnov;

import helpers.ConfigHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ObjectDecoder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

//todo use config
public class Server {

    public static final String CONFIG_FILE = "server.config";
    public static final int MAX_OBJECT_SIZE = 1024 * 1024;

    private int port;
    private InetAddress address;
    ConfigHelper config;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public Server() throws UnknownHostException {
        this(INET_PORT, InetAddress.getLocalHost());
    }

    //todo add checkport
    public Server(int port, InetAddress address) {
        this.port = port;
        this.address = address;
        this.config = Path.of(CONFIG_FILE)
    }

    public static void main(String[] args) throws Exception {
        new Server().run();
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            logger.log(Level.INFO, "Server is starting");

            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(new ObjectDecoder(MAX_OBJECT_SIZE, null), new AuthHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(this.address, this.port).sync();

            logger.log(Level.INFO, "Server is started");

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
