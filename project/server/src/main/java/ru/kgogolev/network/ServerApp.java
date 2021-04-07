package ru.kgogolev.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import ru.kgogolev.FileDecoder;
import ru.kgogolev.PortHost;
import ru.kgogolev.network.in_handler.AuthHandler;
import ru.kgogolev.network.in_handler.InputHandler;
import ru.kgogolev.network.out_handler.OutputHandler;

public class ServerApp {
    public static void main(String[] args) {
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
                                    new OutputHandler(),
                                    new InputHandler(new FileDecoder(WorkingDirectory.WORKING_DIRECTORY))
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
