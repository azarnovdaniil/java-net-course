package ru.daniilazarnov;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Network {
    private SocketChannel channel;
    public Network() {
        new Thread(()-> {
            EventLoopGroup dataManagementGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(dataManagementGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel=socketChannel;
                            }
                        });
                ChannelFuture future = b.connect("localhost", 8189);

            }   ///37-10
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
