package ru.daniilazarnov.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;

public class MyInboundHandlerFirst extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("first");
        ByteBuf buf = (ByteBuf) msg;
        if (buf.readableBytes() < 3) {
            buf.release();
            ctx.writeAndFlush("Not worked");
        }
        byte[] data = new byte[3];
        buf.readBytes(data);
        buf.release();
        System.out.println(Arrays.toString(data));
        ctx.fireChannelRead(data);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
