package ru.daniilazarnov.handler;

import java.util.logging.Logger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.util.interaction.Message;

public class MessageHandler extends ChannelInboundHandlerAdapter {
  private static final Logger LOGGER = Logger.getLogger(MessageHandler.class.getName());

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    Message m = new Message();
    m.setFileName("FUCK YOU CLIENT!");
    ctx.writeAndFlush(m);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    System.out.println(msg.getClass().getName());
    Message m = (Message) msg;
    System.out.println(m);
    ctx.close();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) {
    LOGGER.info("Client disconnected...");
  }
}
