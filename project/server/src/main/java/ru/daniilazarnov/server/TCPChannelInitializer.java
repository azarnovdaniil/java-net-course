package ru.daniilazarnov.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class TCPChannelInitializer extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel socketChannel) {
    socketChannel.pipeline().addLast(new DiscardServerHandler());
  }
}
