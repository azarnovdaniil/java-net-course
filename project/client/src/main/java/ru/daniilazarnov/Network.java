package ru.daniilazarnov;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import ru.daniilazarnov.handler.MessageHandler;


public class Network {
  private static final Logger LOGGER = Logger.getLogger(Network.class.getName());
  private final String HOST;
  private final int PORT;
  private Channel currentChannel;

  public Network() throws IOException {
    try {
      Properties property = new Properties();
      property.load(getFileFromResourceAsStream("config.properties"));
      PORT = Integer.parseInt(property.getProperty("port"));
      HOST = property.getProperty("host");

    } catch (IOException e) {
      throw new FileNotFoundException("Property file not found");
    }
  }

  public void start(CountDownLatch countDownLatch) {
    LOGGER.info("Connecting to server at port: " + PORT);
    LOGGER.info("Connecting to server at host: " + HOST);

    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap clientBootstrap = new Bootstrap();
      clientBootstrap.group(group)
        .channel(NioSocketChannel.class)
        .remoteAddress(new InetSocketAddress(HOST, PORT))
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          public void initChannel(SocketChannel socketChannel) {
            socketChannel.pipeline().addLast(new MessageHandler());
            currentChannel = socketChannel;
          }
        });

      ChannelFuture channelFuture = clientBootstrap.connect(HOST, PORT).sync();
      countDownLatch.countDown();
      channelFuture.channel().closeFuture().sync();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        group.shutdownGracefully().sync();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
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

  public Channel getCurrentChannel() {
    return currentChannel;
  }

  public void stop() {
    currentChannel.close();
  }
}
