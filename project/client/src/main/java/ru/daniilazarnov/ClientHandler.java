package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    /*
     * Print chat message received from server.
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("Message: " + msg);
        if (msg.equals("-> [Lina]: command")) {
            System.out.println("Поступила команда");
        } else {
            System.out.println("комманда не поступила" + msg);
        }

    }

}