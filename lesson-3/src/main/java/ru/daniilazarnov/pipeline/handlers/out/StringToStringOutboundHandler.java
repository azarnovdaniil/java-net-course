package ru.daniilazarnov.pipeline.handlers.out;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class StringToStringOutboundHandler extends ChannelOutboundHandlerAdapter {

    // write -> write -> write
    // read -> read -> read

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("string to byte 2");
        String str = (String) msg;
        byte[] arr = (str + " StringToByteBufHandler2 ").getBytes();
        ByteBuf buf = ctx.alloc().buffer(arr.length);
        buf.writeBytes(arr);
        ctx.writeAndFlush(buf);
    }
}
