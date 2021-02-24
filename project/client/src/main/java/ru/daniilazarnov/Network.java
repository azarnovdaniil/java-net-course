package ru.daniilazarnov;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Network {

    private io.netty.channel.socket.SocketChannel channel;
    private static final int PORT = 8189;
    private static final String HOST = "localhost";
    private Callback onMsgReceivedCallback;

    public Network(Callback onMsgReceivedCallback) {
        this.onMsgReceivedCallback = onMsgReceivedCallback;
        Thread t1 = new Thread(() -> {
            NioEventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {
                            @Override
                            protected void initChannel(io.netty.channel.
                                                               socket.SocketChannel socketChannel) throws Exception {
                                channel = socketChannel;
                                socketChannel.pipeline().addLast(new StringDecoder(), new StringEncoder(),
                                        new SimpleChannelInboundHandler<String>() {
                                            @Override
                                            protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                                                        String s) throws Exception {
                                                if (onMsgReceivedCallback != null) {
                                                    onMsgReceivedCallback.callback(s);
                                                }
                                            }
                                        });
                            }
                        });
                ChannelFuture future = b.connect(HOST, PORT).sync();
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        });
        t1.setDaemon(true);
        t1.start();
    }

    public void sendMessage(String str) {
        channel.writeAndFlush(str);
    }

    public void close() {
        channel.close();
    }
}
