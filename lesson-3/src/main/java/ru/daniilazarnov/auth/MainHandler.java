package ru.daniilazarnov.auth;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

class MainHandler extends ChannelInboundHandlerAdapter {

    private final String username;

    MainHandler(String username) {
        this.username = username;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String input = (String) msg;
        System.out.println(username + ": " + input);
    }
}
