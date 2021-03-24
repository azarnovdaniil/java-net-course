package ru.kgogolev;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.nio.charset.StandardCharsets;

public class ClientOutputHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//        String mes = (String) msg;
//        String word = mes.split(" ")[1];
//        String newWord = word.concat(word);
//        ByteBuf buff = Unpooled.copiedBuffer(newWord.getBytes(StandardCharsets.UTF_8));
        ByteBuf buff = Unpooled.copiedBuffer((byte[]) msg);
        ctx.writeAndFlush(buff);
    }
}
