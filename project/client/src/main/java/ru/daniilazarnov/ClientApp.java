package ru.daniilazarnov;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientApp {

    public ClientApp() {
        final EventLoopGroup[] workerGroup = {new NioEventLoopGroup()};
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup[0]);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new Handler());
                }
            });
            Channel f = b.connect("localhost", 8189).sync().channel();
            MessageHandler messageHandler = new MessageHandler(f);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }  finally {
            workerGroup[0].shutdownGracefully();
        }
    }
}
