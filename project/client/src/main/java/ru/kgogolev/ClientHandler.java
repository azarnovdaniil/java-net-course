package ru.kgogolev;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        ByteBuf buffer = (ByteBuf) msg;
        while (buffer.readableBytes()>0) {
            System.out.print((char)buffer.readByte());
            System.out.flush();
        }

    }
}
