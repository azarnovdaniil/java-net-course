package ru.kgogolev.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import ru.kgogolev.*;
import ru.kgogolev.network.in_handler.AuthHandler;
import ru.kgogolev.network.in_handler.ServerInputHandler;
import ru.kgogolev.network.out_handler.ServerOutputHandler;

public class Server {
    private User serverUser;

    public Server(User serverUser) {
        this.serverUser = serverUser;
        run();
    }

    private void run() {
        EventLoopGroup clients = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(clients)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new AuthHandler(),
                                    new ServerOutputHandler(new FileEncoder()),
                                    new ServerInputHandler(serverUser.getRootNavigateDirectory(),
                                            new FileSystem(),
                                            new FileDecoder(serverUser.getRootDownloadDirectory()))
                            );
                        }
                    });
            ChannelFuture cf = bootstrap.bind(PortHost.PORT).sync();
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clients.shutdownGracefully();
        }

    }
}
