package ru.daniilazarnov.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
  private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
  private final int port;


  public Server() throws IOException {
    try {
      Properties property = new Properties();
      property.load(getFileFromResourceAsStream("config.properties"));
      port = Integer.parseInt(property.getProperty("port"));
    } catch (IOException e) {
      throw new FileNotFoundException("Property file not found");
    }
  }

  public void start() throws InterruptedException {
    LOGGER.info("Starting server at: " + port);

    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(new TCPChannelInitializer())
        .childHandler(new ServerStorageHandler())
        .childOption(ChannelOption.SO_KEEPALIVE, true);

      ChannelFuture f = b.bind(port).sync();
      if (f.isSuccess()) {
        LOGGER.info("Server started successfully");
      }
      f.channel().closeFuture().sync();
    } finally {
      LOGGER.info("Stopping server");
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
  }

  private InputStream getFileFromResourceAsStream(String filename) {
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream(filename);

    if (inputStream == null) {
      throw new IllegalArgumentException("file not found! " + filename);
    } else {
      return inputStream;
    }
  }

}
