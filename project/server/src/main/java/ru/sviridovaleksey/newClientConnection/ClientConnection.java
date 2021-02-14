package ru.sviridovaleksey.newClientConnection;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import java.util.logging.Handler;
import java.util.logging.Logger;


public class ClientConnection {

    private static final Logger logger = Logger.getLogger(ClientConnection.class.getName());
    private final int usePort;




    public ClientConnection(Handler fileHandler, int usePort) {
        logger.addHandler(fileHandler);
        this.usePort = usePort;

    }

    public void startConnection() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) {
                           ch.pipeline().addFirst(new Decoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())),
                                   new EncoderServer(),
                                   new ServerOutHandler(),
                                   new ServerInHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(usePort).sync(); // (7)
            System.out.println("Сервер запущен");
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}






