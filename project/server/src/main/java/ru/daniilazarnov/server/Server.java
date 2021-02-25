package ru.daniilazarnov.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;
import ru.daniilazarnov.handler.MessageHandler;

public class Server {
  private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
  private final int PORT;

  public Server() throws IOException {
    try {
      Properties property = new Properties();
      property.load(getFileFromResourceAsStream("config.properties"));
      PORT = Integer.parseInt(property.getProperty("port"));
    } catch (IOException e) {
      throw new FileNotFoundException("Property file not found");
    }
  }

  public void start() throws InterruptedException {
    LOGGER.info("Starting server at: " + PORT);
    EventLoopGroup bossGroup = new NioEventLoopGroup(2);
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .handler(new LoggingHandler(LogLevel.INFO))
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          public void initChannel(SocketChannel ch) {
            ch.pipeline().addLast(
              new StringEncoder(CharsetUtil.UTF_8),
              new LineBasedFrameDecoder(8192),
              new StringDecoder(CharsetUtil.UTF_8),
              new ChunkedWriteHandler(),
              new MessageHandler()
            );
          }
        })
        .option(ChannelOption.SO_BACKLOG, 128)
        .childOption(ChannelOption.SO_KEEPALIVE, true);

      ChannelFuture f = b.bind(PORT).sync();
      f.channel().closeFuture().sync();
    } finally {
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
