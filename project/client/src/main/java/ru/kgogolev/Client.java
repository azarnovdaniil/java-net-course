package ru.kgogolev;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client {
    private SocketChannel channel;
    private static final String HOST = "localhost";
    private static final int PORT = 9999;
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    public Client() {
        new Thread(() ->
        {
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup);
                b.channel(NioSocketChannel.class);
                b.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        channel = ch;
                        ch.pipeline().addLast(new ClientInputHandler(), new ClientOutputHandler());
                    }
                });

                ChannelFuture f = b.connect(HOST, PORT).sync();

                f.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        }).start();
        listenConsole();
    }

    public void sendMessage(String str) {
        channel.writeAndFlush(str);
    }

    public void listenConsole() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

            while (true) {
                String line = br.readLine();
                if (line.startsWith("download")) {
                    sendMessage(line);
                } else {
                    System.out.println(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

