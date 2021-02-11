package ru.daniilazarnov.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.FunctionalServer;

import java.util.ArrayList;
import java.util.List;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static List<Channel> channels = new ArrayList<>();
    private FunctionalServer functionalServer = new FunctionalServer();
    private String clientName;
    private static int clientIndex = 1;
    private String userPath;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected");
        clientName = "User" + clientIndex;
        clientIndex++;

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        functionalServer.executeCommand(ctx, msg, clientName);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
