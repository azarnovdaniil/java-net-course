package ru.daniilazarnov;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;
import ru.daniilazarnov.encoderdecoder.RequestDecoder;
import ru.daniilazarnov.encoderdecoder.RequestEncoder;
import ru.daniilazarnov.encoderdecoder.ResponseDecoder;
import ru.daniilazarnov.encoderdecoder.ResponseEncoder;

import static ru.daniilazarnov.Constants.PORT;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class);

    public static void main(String[] args) throws Exception {
        int port = Integer.getInteger("m_port", PORT);
        LOGGER.info("Server start!");
        new Server().run(port);
    }

    public void run(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 128);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast("Decoder", new EncoderDecoder.Decoder());
                            ch.pipeline().addLast("Encoder", new EncoderDecoder.Encoder());
                            ch.pipeline().addLast("Server Handler", new ServerHandler());
                        }
                    });

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
