package ru.daniilazarnov.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.Command;
import ru.daniilazarnov.DataMsg;
import ru.daniilazarnov.FunctionalServer;

import java.nio.file.Path;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private FunctionalServer functionalServer = new FunctionalServer();
    private String clientName;
    private static int clientIndex = 1;
    private String generalPath;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected");
        clientName = "User" + clientIndex;
        clientIndex++;
        generalPath = Path.of("project/server/directories/" + clientName).toString();
        ctx.writeAndFlush(DataMsg.createMsg(Command.START, clientName));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        functionalServer.executeCommand(ctx, msg, generalPath);
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
