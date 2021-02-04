package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            // Discard the received data silently.
            System.out.println("Received and released");
            ((ByteBuf) msg).release();
            ctx.writeAndFlush(msg);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
    //@Override
    //public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
    //
    //byteBuf.writeBytes("hello!".getBytes());
    //ctx.writeAndFlush(msg);
    //}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
