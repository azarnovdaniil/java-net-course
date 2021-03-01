package ru.daniilazarnov;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class Client {

    final static String HOST = "127.0.0.1";
    final static int PORT = 8189;
    final private static int maxObjectSize = 104857600;

    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new ObjectDecoder(maxObjectSize, ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new DClientHandler());
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = b.connect(HOST, PORT).sync();
            channelFuture.sync().channel();
            channelFuture.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }
    }
}