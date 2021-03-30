package ru.kgogolev.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import ru.kgogolev.PortHost;
import ru.kgogolev.network.in_handler.ClientInputHandler;
import ru.kgogolev.network.out_handler.ClientOutputHandler;

public class Client {
    private SocketChannel channel;


    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public Client() {
        new Thread(() -> {
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

                ChannelFuture f = b.connect(PortHost.HOST, PortHost.PORT).sync();

                f.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        }).start();
    }

    public void sendMessage(ByteBuf bytes) {
        channel.writeAndFlush(bytes);
    }


}

