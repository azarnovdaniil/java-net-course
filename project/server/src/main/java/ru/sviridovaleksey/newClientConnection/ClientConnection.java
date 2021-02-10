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
import ru.sviridovaleksey.clientHandler.DataBaseUser;
import ru.sviridovaleksey.workwithfiles.WorkWithFile;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Logger;


public class ClientConnection {

    private static final Logger logger = Logger.getLogger(ClientConnection.class.getName());
    private int usePort;
    private DataBaseUser dataBaseUser = new DataBaseUser();



    public ClientConnection(Handler fileHandler, int usePort) throws IOException {
        this.logger.addHandler(fileHandler);
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
                        public void initChannel(SocketChannel ch) throws Exception {
                           ch.pipeline().addFirst(new Decoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())),
                                   new EncoderServer(),
                                   new ServerOutHandler(),
                                   new ServerInHandler(dataBaseUser));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(usePort).sync(); // (7)

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}






