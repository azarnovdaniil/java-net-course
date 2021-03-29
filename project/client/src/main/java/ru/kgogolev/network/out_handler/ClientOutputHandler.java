package ru.kgogolev.network.out_handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;



public class ClientOutputHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//        ByteBuf buff = Unpooled.copiedBuffer((byte[]) msg);
//        ctx.writeAndFlush(buff);
        ctx.writeAndFlush(msg);
    }
}
