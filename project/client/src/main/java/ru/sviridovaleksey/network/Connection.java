package ru.sviridovaleksey.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;



public class Connection {

    private final String serverAddress;
    private final int usePort;



    public Connection(String serverAddress, int usePort) {
        this.serverAddress = serverAddress;
        this.usePort = usePort;
        openConnection();

    }

   public void openConnection() {
       EventLoopGroup workerGroup = new NioEventLoopGroup();

       try {
           Bootstrap b = new Bootstrap();
           b.group(workerGroup);
           b.channel(NioSocketChannel.class);
           b.option(ChannelOption.SO_KEEPALIVE, true);
           b.handler(new ChannelInitializer<SocketChannel>() {
               @Override
               public void initChannel(SocketChannel socketChannel) {
                   socketChannel.pipeline().addFirst(new Encoder(),
                           new DecoderClient(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())),
                           new NettyOutHandler(), new NettyInHandler());
               }
           });

           ChannelFuture f = b.connect(serverAddress, usePort).sync();
           f.channel().closeFuture().sync();
       } catch (InterruptedException e) {
           e.printStackTrace();
       } finally {
           workerGroup.shutdownGracefully();
       }
   }


}
