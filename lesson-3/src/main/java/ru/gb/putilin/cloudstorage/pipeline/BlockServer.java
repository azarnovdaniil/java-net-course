package ru.gb.putilin.cloudstorage.pipeline;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import ru.gb.putilin.cloudstorage.pipeline.handlers.in.FinalHandler;
import ru.gb.putilin.cloudstorage.pipeline.handlers.in.FirstHandler;
import ru.gb.putilin.cloudstorage.pipeline.handlers.in.GatewayHandler;
import ru.gb.putilin.cloudstorage.pipeline.handlers.in.SecondHandler;
import ru.gb.putilin.cloudstorage.pipeline.handlers.out.StringToByteBufHandler;
import ru.gb.putilin.cloudstorage.pipeline.handlers.out.StringToByteBufHandler2;

public class BlockServer {

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(new StringToByteBufHandler2())
                                    .addLast(new FirstHandler())
                                    .addLast(new StringToByteBufHandler())
                                    .addLast(new SecondHandler())
                                    .addLast(new GatewayHandler())
                                    .addLast(new FinalHandler());
                        }
                    });
            ChannelFuture f = b.bind(8189).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new BlockServer().run();
    }
}
