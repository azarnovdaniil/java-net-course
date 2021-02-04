package ru.daniilazarnov;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import ru.daniilazarnov.discard.DiscardServerHandler;
import ru.daniilazarnov.pipeline.handlers.in.FinalInboundHandler;
import ru.daniilazarnov.pipeline.handlers.in.FirstInboundHandler;
import ru.daniilazarnov.pipeline.handlers.in.GatewayInboundHandler;
import ru.daniilazarnov.pipeline.handlers.in.SecondInboundHandler;
import ru.daniilazarnov.pipeline.handlers.out.StringToByteBufOutboundHandler;
import ru.daniilazarnov.pipeline.handlers.out.StringToStringOutboundHandler;

public class Server {

    public static void main(String[] args) throws Exception {
        new Server().run();
        System.out.println("Server is started up!");
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                            .addLast(new DiscardServerHandler())
                            //.addLast(new ())
                                    // .addLast(new StringToStringOutboundHandler())
                                    // .addLast(new FirstInboundHandler())
                                    // .addLast(new StringToByteBufOutboundHandler())
                                    // .addLast(new SecondInboundHandler())
                                    // .addLast(new GatewayInboundHandler())
                                    // .addLast(new FinalInboundHandler())
                            ;
                        }
                    });
            ChannelFuture future = bootstrap.bind(8189).sync();
            future.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
