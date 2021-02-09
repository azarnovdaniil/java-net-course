package ru.daniilazarnov.pipeline.handlers.in;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.Arrays;

public class FirstInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("FirstInboundHandler first");
        ByteBuf buf = (ByteBuf) msg;
        String str = buf.toString(CharsetUtil.UTF_8);
        System.out.println("прочитано "+str);
        if (buf.readableBytes() < 3) {
            buf.release();
            ctx.writeAndFlush("hahahah");  // отправка сообщения
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
