package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TCPChannelHandler extends SimpleChannelInboundHandler<String> {

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    ctx.channel().remoteAddress();
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
    ctx.channel().remoteAddress();
    ctx.channel().writeAndFlush("Thanks\n");
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) {
    ctx.channel().remoteAddress();
  }
}
