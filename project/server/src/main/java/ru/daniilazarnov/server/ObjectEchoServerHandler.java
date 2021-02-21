package ru.daniilazarnov.server;

import java.util.concurrent.atomic.AtomicLong;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class ObjectEchoServerHandler extends SimpleChannelUpstreamHandler {

  private final AtomicLong transferredMessages = new AtomicLong();

  public long getTransferredMessages() {
    return transferredMessages.get();
  }

  @Override
  public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
    if (e instanceof ChannelStateEvent && ((ChannelStateEvent) e).getState() != ChannelState.INTEREST_OPS) {
      System.err.println(e);
    }
    super.handleUpstream(ctx, e);
  }

  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
    // Echo back the received object to the client.
    transferredMessages.incrementAndGet();
    e.getChannel().write(e.getMessage());
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
    e.getCause().printStackTrace();
    e.getChannel().close();
  }
}
