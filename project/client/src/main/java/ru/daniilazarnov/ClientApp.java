package ru.daniilazarnov;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class ClientApp {
    private SocketChannel socket;
    private static final String HOST = "localhost";
    private static final int PORT = 8189;
    public ClientApp(){
        new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socket = socketChannel;
                                socketChannel.pipeline().addLast(
                                        new ObjectDecoder(1024 * 1024 * 100, ClassResolvers.cacheDisabled(null)),
                                        new ObjectEncoder(),
                                        new ClientHandler());
                            }
                        });
                ChannelFuture channelFuture = bootstrap.connect(HOST, PORT).sync();
                channelFuture.channel().closeFuture().sync();
            } catch (Exception e){
                e.printStackTrace();
            }finally {
                workerGroup.shutdownGracefully();
            }
        }).start();
    }

    public void sendMessage(String message){
        socket.writeAndFlush(message);
    }

}
