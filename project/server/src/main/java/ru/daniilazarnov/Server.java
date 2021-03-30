package ru.daniilazarnov;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import ru.daniilazarnov.handlers.in.ProcessingHandler;
import ru.daniilazarnov.handlers.in.RequestDecoder;
import ru.daniilazarnov.handlers.out.ResponseDataEncoder;

public class Server {

    private int port;
    public static final int SIZE = 128;
    public static final int PN = 8188;

    public Server(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(new RequestDecoder())
                                    .addLast(new ResponseDataEncoder())
                                    .addLast(new ProcessingHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, SIZE)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = args.length > 0
                ? Integer.parseInt(args[0]) : PN;

        new Server(port).run();
    }
}
