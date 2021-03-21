package ru.kgogolev;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

public class ServerApp {
    private static final int PORT = 9999;
    public static void main(String[] args) {
        EventLoopGroup clients = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(clients)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new InputHandler()
                        );
                        }
                    });
            ChannelFuture cf = bootstrap.bind(PORT).sync();
            cf.channel().closeFuture().sync();//блокирующая операция - ожидание остановки сервера
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            clients.shutdownGracefully();
        }

    }
}
