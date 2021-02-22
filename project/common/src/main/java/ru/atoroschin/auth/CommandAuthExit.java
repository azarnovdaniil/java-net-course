package ru.atoroschin.auth;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import ru.atoroschin.Credentials;
import ru.atoroschin.FileWorker;

public class CommandAuthExit implements CommandAuth {
    @Override
    public void send(ChannelHandlerContext ctx, Credentials credentials, byte signal) {
        final int minLength = 5;
        ByteBuf byBuf = ByteBufAllocator.DEFAULT.buffer();
        byBuf.writeByte(signal);
        byBuf.writeInt(minLength);
        ctx.writeAndFlush(byBuf);
        ctx.fireChannelUnregistered();
        ctx.channel().close();
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService, FileWorker fileWorker, byte signal) {
        ctx.fireChannelUnregistered();
        ctx.channel().close();
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService) {

    }
}
