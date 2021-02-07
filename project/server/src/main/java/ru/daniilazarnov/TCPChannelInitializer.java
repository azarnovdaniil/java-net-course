package ru.daniilazarnov;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TCPChannelInitializer  extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    socketChannel.pipeline().addLast(new StringEncoder());
    socketChannel.pipeline().addLast(new StringDecoder());
    socketChannel.pipeline().addLast(new TCPChannelHandler());
  }
}
