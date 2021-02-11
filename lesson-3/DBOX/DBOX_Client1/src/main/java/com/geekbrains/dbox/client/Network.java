package com.geekbrains.dbox.client;

import com.geekbrains.dbox.client.Handler.in.HandlerInReadBytes;
import com.geekbrains.dbox.client.Handler.in.HandlerInSwitchComand;
import com.geekbrains.dbox.client.Handler.out.HandlerOutToByte;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class Network {
    static public int idUser = 0;

    private SocketChannel channel;
    public Network() {
        new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try{
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {

                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel;
                                socketChannel.pipeline().addLast(new HandlerInReadBytes());
                                socketChannel.pipeline().addLast(new HandlerInSwitchComand());
                                socketChannel.pipeline().addLast(new HandlerOutToByte());

                            }
                        });
                ChannelFuture future = b.connect("localhost", 8888).sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }

        }).start();
    }

    public void sendMessage(Object obj){

        channel.writeAndFlush(obj);
    }

    public String[] strToArr_Sep(String str){
        return str.split(Separator.stringSep.getCode());
    }
    public void close(){
        channel.close();
    }
}
