package ru.daniilazarnov.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.Command;
import ru.daniilazarnov.DataMsg;
import ru.daniilazarnov.FunctionalServer;

import java.util.ArrayList;
import java.util.List;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static List<Channel> channels = new ArrayList<>();
    private FunctionalServer functionalServer = new FunctionalServer();
    private String clientName;
    private static int clientIndex = 1;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected");
        clientName = "User" + clientIndex;
        clientIndex++;
        ctx.writeAndFlush(new DataMsg(Command.START, null));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        functionalServer.executeCommand(ctx, msg, clientName);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().close();
    }
}