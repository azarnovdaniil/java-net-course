package ru.daniilazarnov.client.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import ru.daniilazarnov.client.Client;
import ru.daniilazarnov.client.configuration.ClientConfiguration;
import ru.daniilazarnov.client.responses.ResponseHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

public class ClientConnection {

    private Channel currentChannel;
    private final int port;
    private final String host;
    private final ClientConfiguration clientConfiguration;

    public ClientConnection(String host, int port, ClientConfiguration clientConfiguration) {
        this.port = port;
        this.host = host;
        this.clientConfiguration = clientConfiguration;
    }

    public Channel getCurrentChannel() {
        return currentChannel;
    }

    public void start(CountDownLatch countDownLatch) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(new ResponseHandler(clientConfiguration.getDownloads()));
                            currentChannel = socketChannel;
                        }
                    });
            ChannelFuture channelFuture = clientBootstrap.connect().sync();
            countDownLatch.countDown();
            try {
                new Client(currentChannel).run();
            } catch (IOException e) {
                e.printStackTrace();
            }
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        currentChannel.close();
    }


}
