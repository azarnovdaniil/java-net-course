package ru.daniilazarnov;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

  public void start(int port) throws InterruptedException {
    System.out.println("Starting server at: " + port);
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(new TCPChannelInitializer())
        .childOption(ChannelOption.SO_KEEPALIVE, true);

      // Bind and start to accept incoming connections.
      ChannelFuture f = b.bind(port).sync();
      if (f.isSuccess()) {
        System.out.println("Server started successfully");
      }
      f.channel().closeFuture().sync();
    } finally {
      System.out.println("Stopping server");
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
  }
}
