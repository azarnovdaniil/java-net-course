package ru.atoroschin.commands;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.atoroschin.AuthService;
import ru.atoroschin.FileWorker;

public interface CommandAuth {
    void send(ChannelHandlerContext ctx, String content, byte signal);

    void response(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService, FileWorker fileWorker, byte signal);

    void receive(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService);
}
