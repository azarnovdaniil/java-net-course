package ru.daniilazarnov;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.LineEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;
import java.util.HashSet;

import static io.netty.buffer.Unpooled.wrappedBuffer;

public class RepoServer {
    private final int port;
    private final String host;
    private HashSet<UserProfile> userList;
    private volatile ContextData contextData;



    RepoServer (String host, int port){
        this.port=port;
        this.host=host;
        this.userList = new HashSet<UserProfile>();
        this.contextData=new ContextData();
    }

    public void start(){
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(workGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(this.host,this.port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    userList.add(new UserProfile("UserUnknown",socketChannel));
                    System.out.println("New connection applied.");

                    socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1300,contextData.getDelimiter()));
                    //socketChannel.pipeline().addLast(new StringDecoder());
                    socketChannel.pipeline().addLast(new RepoDecoder());
                    socketChannel.pipeline().addLast(new RepoEncoder(contextData));
                    socketChannel.pipeline().addLast(new ChunkedWriteHandler());
                    socketChannel.pipeline().addLast(new IncomingFileHandler());

                }
            });
            System.out.println("Server is up and ready for connections...");
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            try {
                workGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
