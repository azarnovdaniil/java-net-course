package ru.daniilazarnov;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import ru.daniilazarnov.handlers.AuthHandler;
import ru.daniilazarnov.handlers.FilesHandler;
import ru.daniilazarnov.handlers.RequestHandler;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private static int port;

    public Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure("./project/server/log4j.properties");
        LOGGER.debug("Клиент подключившийся с localhost " + port);
        new Server(8189).run();
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
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ObjectDecoder(1024 * 1024 * 100, ClassResolvers.cacheDisabled(null)))
                                    .addLast(new ObjectEncoder())
                                    .addLast(new AuthHandler())
                                    .addLast(new RequestHandler())
                                    .addLast(new FilesHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}