package ru.atoroschin.commands;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import ru.atoroschin.AuthService;
import ru.atoroschin.CommandsAuth;
import ru.atoroschin.FileWorker;

public class CommandAuthErr implements CommandAuth {
    @Override
    public void send(ChannelHandlerContext ctx, String content, byte signal) {

    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService, FileWorker fileWorker, byte signal) {
        final int minLength = 5;
        ByteBuf byBuf = ByteBufAllocator.DEFAULT.buffer();
        byBuf.writeByte(signal);
        byBuf.writeInt(minLength);
        ctx.writeAndFlush(byBuf);
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService) {

    }
}
