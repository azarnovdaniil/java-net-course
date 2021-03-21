package ru.daniilazarnov;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.LineEncoder;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;

public class RepoClient {
    private final int port;
    private final String host;
    private SocketChannel curChannel;
    private  volatile  ContextData contextData;

    RepoClient (String host, int port){
        this.port=port;
        this.host=host;
        this.contextData=new ContextData();

    }

    public void start(){
        new Thread (()-> {
            EventLoopGroup workGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workGroup)
                        .channel(NioSocketChannel.class)
                        .remoteAddress(new InetSocketAddress(this.host, this.port))
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                curChannel = socketChannel;
                                System.out.println("Connected to server...");
                                socketChannel.pipeline().addLast(new RepoDecoder());
                                socketChannel.pipeline().addLast(new RepoEncoder(contextData));
                                socketChannel.pipeline().addLast(new OutcomingFilehandler());
                                socketChannel.pipeline().addLast(new ChunkedWriteHandler());


                            }
                        });
                ChannelFuture f = b.connect().sync();
                f.channel().closeFuture().sync();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    workGroup.shutdownGracefully().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    public void sendFile (Path toSend){
        contextData.setCommand(1);
        contextData.setFileName(toSend.toFile().getName());
        try {
            this.curChannel.writeAndFlush(new ChunkedFile(toSend.toFile(), 800));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile (String name){
        contextData.setCommand(2);
        contextData.setFileName(name);
        this.curChannel.writeAndFlush(new byte[1]);
    }
}
