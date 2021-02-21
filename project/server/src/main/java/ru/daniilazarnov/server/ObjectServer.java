package ru.daniilazarnov.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
public class ObjectServer {
  static final boolean SSL = System.getProperty("ssl") != null;
  static final int PORT = Integer.parseInt(System.getProperty("port", "8991"));

  public static void main(String[] args) throws Exception {
    // Configure SSL.


    // Configure the server.
    ServerBootstrap bootstrap = new ServerBootstrap(
      new NioServerSocketChannelFactory(
        Executors.newCachedThreadPool(),
        Executors.newCachedThreadPool()));

    // Set up the pipeline factory.
    bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
      public ChannelPipeline getPipeline() {
        ChannelPipeline p = Channels.pipeline(
          new ObjectEncoder(),
          new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())),
          new ObjectEchoServerHandler());

        return p;
      }
    });

    // Bind and start to accept incoming connections.
    bootstrap.bind(new InetSocketAddress(PORT));
  }
}
