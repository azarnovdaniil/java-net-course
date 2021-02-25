package ru.daniilazarnov.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.util.interaction.Message;

public class MessageHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    Message m = (Message) msg;
    System.out.println(m.getFileName());
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {

  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }


}
