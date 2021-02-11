package ru.daniilazarnov.commands;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

public class CommandUnknown implements Command {

    @Override
    public void send(ChannelHandlerContext ctx, byte signal) {
        System.out.println("Неизвестная команда");
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf) {
        // do something
    }
}
