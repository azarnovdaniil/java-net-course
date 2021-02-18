package ru.daniilazarnov;
import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.ChannelFuture;

import io.netty.channel.ChannelInitializer;

import io.netty.channel.ChannelOption;

import io.netty.channel.EventLoopGroup;

import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.SocketChannel;

import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.logging.Level;


public class Server {

    public static void main(String[] args) {

        Log.startLog();

        try {

            new Server().run();

        } catch (Exception e) {

            Log.protocolLogger.log(Level.WARNING, "Сервер не запускается :( Пожалуйста попробуйте еще раз!");

            Log.echoLogger.log(Level.WARNING, "Сервер не запускается :( Пожалуйста попробуйте еще раз!");

        }

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
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new Handler(), new EchoHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = b.bind(8189).sync();
            Database.connect();
            Log.protocolLogger.log(Level.INFO, "Сервер запущен!");
            Log.echoLogger.log(Level.INFO, "Сервер запущен!");
            future.channel().closeFuture().sync();
        } finally {
            Database.disconnect();
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}