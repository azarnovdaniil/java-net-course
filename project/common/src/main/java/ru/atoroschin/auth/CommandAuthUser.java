package ru.atoroschin.auth;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.atoroschin.*;
import ru.atoroschin.auth.AuthService;
import ru.atoroschin.auth.CommandAuth;

import java.util.List;

public class CommandAuthUser implements CommandAuth {

    @Override
    public void send(ChannelHandlerContext ctx, Credentials credentials, byte signal) {
        List<String> list = List.of(credentials.getLogin());
        ByteBuf bufOut = BufWorker.makeBufFromList(list, signal);
        ctx.fireChannelRead(bufOut);
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService,
                         FileWorker fileWorker, byte signal) {
        List<String> list = BufWorker.readFileListFromBuf(buf);
        String userLogin = list.get(0);
        try {
            fileWorker.appendBasePath(authService.getUserFolder(userLogin));
            fileWorker.setMaxVolume(authService.getMaxVolume(userLogin));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService) {

    }
}
