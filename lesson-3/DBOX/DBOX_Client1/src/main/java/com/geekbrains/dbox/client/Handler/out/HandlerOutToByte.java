package com.geekbrains.dbox.client.Handler.out;

import com.geekbrains.dbox.client.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;


public class HandlerOutToByte extends ChannelOutboundHandlerAdapter {
    public  void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Message m = (Message) msg;
        byte[] b = m.HandlerOutToByte();
        ByteBuf buf = ctx.alloc().buffer(b.length);
        buf.writeBytes(b);
       // System.out.print("<out> " + "<" + b[0] + ">" + b.length);
        ctx.writeAndFlush(buf);
    }
}
