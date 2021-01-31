package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;


public class InStringHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println(msg.getClass().getSimpleName());
        ByteBuf buf = (ByteBuf) msg;
        String s = buf.toString(StandardCharsets.UTF_8);
        System.out.println(s);
        ctx.fireChannelRead(s);
    }
}