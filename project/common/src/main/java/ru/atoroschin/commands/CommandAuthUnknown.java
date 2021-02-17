package ru.atoroschin.commands;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.atoroschin.AuthService;

public class CommandAuthUnknown implements CommandAuth {
    @Override
    public void send(ChannelHandlerContext ctx, String content, byte signal) {

    }

    @Override
    public int response(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService, byte signal) {

    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService) {

    }
}
