package ru.daniilazarnov.pipeline;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import ru.daniilazarnov.pipeline.handlers.in.FinalInboundHandler;
import ru.daniilazarnov.pipeline.handlers.in.FirstInboundHandler;
import ru.daniilazarnov.pipeline.handlers.in.GatewayInboundHandler;
import ru.daniilazarnov.pipeline.handlers.in.SecondInboundHandler;
import ru.daniilazarnov.pipeline.handlers.out.StringToStringOutboundHandler;
import ru.daniilazarnov.pipeline.handlers.out.StringToByteBufOutboundHandler;

public class MyServer {

    public static final int INET_PORT = 8189;

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
                                    .addLast(new StringToByteBufOutboundHandler()) // 1 - outbound
                                    .addLast(new FirstInboundHandler()) // 1 - inbound
                                    .addLast(new StringToStringOutboundHandler()) // 2 - outbound
                                    .addLast(new SecondInboundHandler()) // 2 - inbound
                                    .addLast(new GatewayInboundHandler()) // 3 - inbound
                                    .addLast(new FinalInboundHandler()); // 4 inbound
                        }
                    });
            ChannelFuture f = b.bind(INET_PORT).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new MyServer().run();
    }
}
