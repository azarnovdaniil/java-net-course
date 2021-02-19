package ru.daniilazarnov;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;
import ru.daniilazarnov.util.ProgressBar;


/**
 * Класс содержит логику работы с сетью на стороне сервера
 */
public class ServerApp {
    private static final Logger LOG = Logger.getLogger(ServerApp.class);
    private static final int PORT = 8189;
    private static final byte EIGHTS = 8;

    public static void main(String[] args) throws Exception {
        new ServerApp().run();
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new ServerHandler());
                }
            });
            ChannelFuture f = b.bind(PORT).sync();
            ProgressBar.start(EIGHTS);
            String s = "SERVER: Launched, waiting for connections ...";
            LOG.info(s);
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
