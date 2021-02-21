package ru.daniilazarnov;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class ClientHandler extends SimpleChannelUpstreamHandler {
  private final List<Integer> firstMessage;
  private final AtomicLong transferredMessages = new AtomicLong();

  public ClientHandler(int firstMessageSize) {
    if (firstMessageSize <= 0) {
      throw new IllegalArgumentException("firstMessageSize: " + firstMessageSize);
    }
    firstMessage = new ArrayList<Integer>(firstMessageSize);
    for (int i = 0; i < firstMessageSize; i ++) {
      firstMessage.add(i);
    }
  }

  public long getTransferredMessages() {
    return transferredMessages.get();
  }

  @Override
  public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
    if (e instanceof ChannelStateEvent &&
      ((ChannelStateEvent) e).getState() != ChannelState.INTEREST_OPS) {
      System.err.println(e);
    }
    super.handleUpstream(ctx, e);
  }

  @Override
  public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
    // Send the first message if this handler is a client-side handler.
    e.getChannel().write(firstMessage);
  }

  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
    // Echo back the received object to the server.
    transferredMessages.incrementAndGet();
    e.getChannel().write(e.getMessage());
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
    e.getCause().printStackTrace();
    e.getChannel().close();
  }
}
