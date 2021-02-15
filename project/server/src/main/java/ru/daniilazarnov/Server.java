package ru.daniilazarnov;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Server {
    public String HOST;
    public  int PORT;
    ServerHandler SRVHAND = new ServerHandler();

    public Server(String host, int port) {
        this.HOST = host;
        this.PORT = port;
    }

    public void run() throws InterruptedException, IOException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline().addLast(
                                    new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()),
                                    new StringDecoder(),
                                    new StringEncoder(),
                                    new ServerHandler()
                            );
                        }
                    });
            bootstrap.bind(HOST, PORT).sync().channel();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                SRVHAND.send(in.readLine());
            }

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        new Server("localhost", 8189).run();
    }
}