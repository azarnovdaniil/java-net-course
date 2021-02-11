package ru.daniilazarnov.commands;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

public interface Command {
    void send(ChannelHandlerContext ctx, byte signal);

    void response(ChannelHandlerContext ctx, ByteBuf buf);
}
