package ru.daniilazarnov;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import ru.daniilazarnov.handlers.in.ClientHandler;
import ru.daniilazarnov.handlers.in.ResponseDataDecoder;
import ru.daniilazarnov.handlers.out.RequestDataEncoder;

public class Client {
    public static final int PORT = 8188;
    public static void main(String[] args) throws Exception {

        String host = "localhost";

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                public void initChannel(SocketChannel ch)
                        throws Exception {
                    ch.pipeline().addLast(new RequestDataEncoder())
                            .addLast(new ResponseDataDecoder())
                            .addLast(new ClientHandler());
                }
            });
            ChannelFuture f = b.connect(host, PORT).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
