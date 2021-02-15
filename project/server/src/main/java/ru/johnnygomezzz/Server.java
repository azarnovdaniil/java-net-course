package ru.johnnygomezzz;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import ru.johnnygomezzz.handlers.ServerHandler;

public class Server {
    private static final int SIZE = 100 * 1024 * 1024;
    private static final int PORT = 8189;

    public static void main(String[] args) throws Exception {
        new Server().server();
    }

    public void server() throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                protected void initChannel(SocketChannel socketChannel) throws Exception { socketChannel
                        .pipeline()
                        .addLast(new ObjectDecoder(SIZE, ClassResolvers.cacheDisabled(null)))
                        .addLast(new ObjectEncoder())
                        .addLast(new ServerHandler());
                }
            } );

            ChannelFuture future = b.bind(PORT).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
