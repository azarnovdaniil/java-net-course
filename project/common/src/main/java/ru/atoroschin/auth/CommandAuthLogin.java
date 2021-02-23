package ru.atoroschin.auth;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.atoroschin.BufWorker;
import ru.atoroschin.Credentials;
import ru.atoroschin.FileWorker;

import java.io.IOException;
import java.util.List;

import static ru.atoroschin.CommandsAuth.*;

public class CommandAuthLogin implements CommandAuth {
    @Override
    public void send(ChannelHandlerContext ctx, Credentials credentials, byte signal) {
        List<String> list = List.of(credentials.getLogin(), credentials.getPassword());
        ByteBuf buf = BufWorker.makeBufFromList(list, signal);
        ctx.writeAndFlush(buf);
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService,
                         FileWorker fileWorker, byte signal) throws IllegalAccessException, IOException {
        List<String> list = BufWorker.readFileListFromBuf(buf);

        Credentials credentials = new Credentials(list);
        if (authService.isAuth(credentials)) {
            AUTHUSER.sendToServer(ctx, credentials);
            AUTHOK.receiveAndSend(ctx, null, null, null);
            ctx.pipeline().remove(ctx.handler());
        } else {
            AUTHERR.receiveAndSend(ctx, null, null, null);
        }
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService) {
    }
}
