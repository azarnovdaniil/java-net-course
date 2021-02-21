package ru.atoroschin;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import ru.atoroschin.auth.AuthService;
import ru.atoroschin.auth.BaseAuthService;

import java.nio.ByteOrder;
import java.util.logging.Logger;

public class ServerStorage {
    private static final int SERVER_PORT = 8189;
    private static final int PART_SIZE = 10 * 1024 * 1024;
    private static Logger logger = Logger.getLogger("");
    private int port;
    private AuthService authService;

    public ServerStorage(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        authService = new BaseAuthService();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        final int lengthFieldMes = 4;
        final int countAdjustment = -5;

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(ByteOrder.BIG_ENDIAN, PART_SIZE,
                                            1, lengthFieldMes, countAdjustment, 0, true))
                                    .addLast(new AuthHandler(authService))
                                    .addLast(new InboundHandler(authService));
                        }
                    });
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = SERVER_PORT;
        }
        new ServerStorage(port).run();
    }

}
