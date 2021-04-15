package ru.daniilazarnov.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.Config;
import ru.daniilazarnov.domain.MyMessage;

import java.io.IOException;

public class AuthHandler extends ChannelInboundHandlerAdapter {
    private final int maxLen = 3;
    private boolean auth = false;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client connected...");
            ctx.writeAndFlush(new MyMessage("Welcome to storage"));
            ctx.writeAndFlush(new MyMessage("To get started, register in the repository or log in, enter /help"));
            }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        UserPool.remove(ctx.channel());
        System.out.println("Client disconnected...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        if (auth) {
            ctx.fireChannelRead(msg);
            return;
        } else {
            if (msg instanceof MyMessage) {
                String identifier = ((MyMessage) msg).getText();
                String[] ident = identifier.split(" ");
                String storage = Config.readConfig(Config.DEFAULT_CONFIG).getServerRepo();
                if (ident[0].equals("/auth") && ident.length == maxLen) {
                    ctx.writeAndFlush(new MyMessage((new CommandServer()).authInStorage(ident[1], ident[2])));
                    ctx.pipeline().addLast(new MainHandler());
                    UserPool.add(new User(ident[1], ctx.channel()));
                    auth = true;
                } else if (ident[0].equals("/reg") && ident.length == maxLen) {
                    ctx.writeAndFlush(new MyMessage((new CommandServer()).regInStorage(ident[1], ident[2], storage)));
                } else if (ident[0].equals("/help")) {
                    ctx.writeAndFlush(new MyMessage((new CommandServer()).callHelpManual().toString()));
                } else {
                    ctx.writeAndFlush(new MyMessage("Wrong command, see /help"));
                }
            } else {
                ctx.writeAndFlush(new MyMessage("Wrong type data, see /help"));
            }
        }
    }
}
