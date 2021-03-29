package ru.daniilazarnov.auth;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Set;

class AuthHandler extends ChannelInboundHandlerAdapter {

    private final Set<String> authorizedClients = Set.of("Vasya");

    private boolean authOk = false;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String input = (String) msg;

        if (authOk) {
            ctx.fireChannelRead(input);
            return;
        }
        if (input.split(" ")[0].equals("/auth")) {
            String username = input.split(" ")[1];
            if (authorizedClients.contains(username)) {
                authOk = true;
                ctx.pipeline().addLast(new MainHandler(username));
            }
        }
    }

}
