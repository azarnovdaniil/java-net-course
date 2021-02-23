package ru.atoroschin.auth;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.atoroschin.Credentials;
import ru.atoroschin.FileWorker;

public interface CommandAuth {
    void send(ChannelHandlerContext ctx, Credentials credentials, byte signal);

    void response(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService, FileWorker fileWorker, byte signal) throws IllegalAccessException;

    void receive(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService);
}
