package ru.sviridovaleksey.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import ru.sviridovaleksey.interactionwithuser.HelloMessage;
import ru.sviridovaleksey.interactionwithuser.Interaction;
;
import java.util.Scanner;


public class Connection {

    private String serverAddress;
    private int usePort;
    private Scanner scanner = new Scanner(System.in);


    public Connection(String serverAddress, int usePort) {
        this.serverAddress = serverAddress;
        this.usePort = usePort;
        openConnection();

    }

   public void openConnection () {
       EventLoopGroup workerGroup = new NioEventLoopGroup();

       try {
           Bootstrap b = new Bootstrap(); // (1)
           b.group(workerGroup); // (2)
           b.channel(NioSocketChannel.class); // (3)
           b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
           b.handler(new ChannelInitializer<SocketChannel>() {
               @Override
               public void initChannel(SocketChannel socketChannel) throws Exception {
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
