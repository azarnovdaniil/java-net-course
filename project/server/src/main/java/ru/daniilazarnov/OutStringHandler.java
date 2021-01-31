package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;


public class OutStringHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String str = (String) msg;
        byte[] arr = (str).getBytes();
        ByteBuf buf = ctx.alloc().buffer(arr.length);
        buf.writeBytes(arr);
//        ByteBuf buf = ctx.alloc().buffer();
//        buf.writeCharSequence(str, StandardCharsets.UTF_8);
//        buf.retain();
        System.out.println("string to byte 2");
        ctx.writeAndFlush(buf);
    }
}
