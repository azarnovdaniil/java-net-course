package com.geekbrains.dbox.server.handlers.out;

import com.geekbrains.dbox.server.handlers.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;


public class HandlerOutToByte extends ChannelOutboundHandlerAdapter {
    public  void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Message m = (Message) msg;
        byte[] b = m.HandlerOutToByte();
        ByteBuf buf = ctx.alloc().buffer(b.length);
        buf.writeBytes(b);
        //System.out.println("x -> " + b.length);
        //ctx.channel().writeAndFlush(buf);
        ctx.writeAndFlush(buf);
    }
}
