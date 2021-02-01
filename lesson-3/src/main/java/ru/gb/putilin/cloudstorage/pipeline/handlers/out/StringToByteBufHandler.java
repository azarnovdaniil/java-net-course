package ru.gb.putilin.cloudstorage.pipeline.handlers.out;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class StringToByteBufHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("string to byte");
        String str = (String) msg;
        ctx.writeAndFlush(msg + "StringToByteBufHandler ");
    }
}
