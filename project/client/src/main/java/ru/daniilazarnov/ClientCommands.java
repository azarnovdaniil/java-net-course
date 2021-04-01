package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;

public class ClientCommands {

    public static void sendString(ChannelHandlerContext ctx, String s) {
        ctx.writeAndFlush(s);
    }
}
