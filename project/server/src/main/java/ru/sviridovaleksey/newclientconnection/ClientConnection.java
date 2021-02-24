package ru.sviridovaleksey.newclientconnection;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;

import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientConnection {

    private static final Logger LOGGER = Logger.getLogger(ClientConnection.class.getName());
    private final int usePort;
    private final Handler fileHandler;
    private final Scanner scanner = new Scanner(System.in);




    public ClientConnection(Handler fileHandler, int usePort) {
        this.fileHandler = fileHandler;
        LOGGER.addHandler(fileHandler);
        this.usePort = usePort;
        Thread thread = new Thread(() -> {
            try {
                start();
            } catch (Exception e) {
                e.printStackTrace();
            }
      });
        thread.start();
    }
    public void start() {
        System.out.println("введите команду exit для выхода");
        while (true) {
            String chose = scanner.next();
            if (chose.equals("exit")) {
                shutDownServer();
            }
        }
   }

    public void startConnection(String defAddress) throws Exception {
        final int channelOption = 128;
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws URISyntaxException {
                           ch.pipeline().addFirst(
                                   new Decoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())),
                                   new EncoderServer(),
                                   new ServerOutHandler(),
                                   new ServerInHandler(fileHandler, defAddress));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, channelOption)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(usePort).sync(); // (7)
            LOGGER.log(Level.INFO, "Сервер запущен");
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    public void shutDownServer() {
        System.exit(0);
    }

}






