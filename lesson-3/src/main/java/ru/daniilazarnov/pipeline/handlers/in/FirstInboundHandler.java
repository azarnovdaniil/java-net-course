package ru.daniilazarnov.pipeline.handlers.in;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;

public class FirstInboundHandler extends ChannelInboundHandlerAdapter {

    public static final int MESSAGE_SIZE = 3;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("first");
        ByteBuf buf = (ByteBuf) msg;
        if (buf.readableBytes() < MESSAGE_SIZE) {
            buf.release();
        }
        byte[] data = new byte[MESSAGE_SIZE];
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
