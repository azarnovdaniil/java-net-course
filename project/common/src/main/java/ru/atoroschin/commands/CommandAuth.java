package ru.atoroschin.commands;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.atoroschin.AuthService;

public interface CommandAuth {
    void send(ChannelHandlerContext ctx, String content, byte signal);

    int response(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService, byte signal);

    void receive(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService);
}
