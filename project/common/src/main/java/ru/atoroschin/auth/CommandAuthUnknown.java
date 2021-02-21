package ru.atoroschin.auth;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.atoroschin.Credentials;
import ru.atoroschin.auth.AuthService;
import ru.atoroschin.FileWorker;
import ru.atoroschin.auth.CommandAuth;

public class CommandAuthUnknown implements CommandAuth {
    @Override
    public void send(ChannelHandlerContext ctx, Credentials credentials, byte signal) {

    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService,
                         FileWorker fileWorker, byte signal) {
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService) {

    }
}
