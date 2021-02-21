package ru.daniilazarnov;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import ru.daniilazarnov.util.command.CommandUtilImpl;

public class Client {
  private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
  static final String HOST = System.getProperty("host", "localhost");
  static final int PORT = Integer.parseInt(System.getProperty("port", "8991"));
  static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));
  public CommandUtilImpl commandPanel;

  public void start() throws InterruptedException {
    LOGGER.info("Starting server at: " + PORT);

    ClientBootstrap bootstrap = new ClientBootstrap(
      new NioClientSocketChannelFactory(
        Executors.newCachedThreadPool(),
        Executors.newCachedThreadPool()));
    try {
      // Set up the pipeline factory.
      bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
        public ChannelPipeline getPipeline() {
          ChannelPipeline p = Channels.pipeline(
            new ObjectEncoder(),
            new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())),
            new ClientHandler(SIZE)
          );

          //fix it
         p.addFirst("xz", new ClientHandler(22));

          return p;
        }
      });

      // Start the connection attempt.
      ChannelFuture f = bootstrap.connect(new InetSocketAddress(HOST, PORT));

      // Wait until the connection attempt is finished and then the connection is closed.
      f.getChannel().getCloseFuture();
    } finally {
      bootstrap.releaseExternalResources();
    }
  }

}
