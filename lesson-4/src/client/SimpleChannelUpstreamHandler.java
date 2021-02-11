package client;

import io.netty.channel.ChannelHandlerContext;

public abstract class SimpleChannelUpstreamHandler {
    public abstract void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception;

    public abstract void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception;

    public abstract void messageReceived(ChannelHandlerContext ctx, MessageEvent e);

    public abstract void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e);
}
