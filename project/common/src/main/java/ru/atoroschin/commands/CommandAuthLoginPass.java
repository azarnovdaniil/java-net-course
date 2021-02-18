package ru.atoroschin.commands;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.atoroschin.AuthService;
import ru.atoroschin.BufWorker;
import ru.atoroschin.FileWorker;

import java.util.List;

import static ru.atoroschin.CommandsAuth.*;

public class CommandAuthLoginPass implements CommandAuth {
    @Override
    public void send(ChannelHandlerContext ctx, String content, byte signal) {
        String[] names = content.split("\\s");
        if (names.length == 2) {
            List<String> list = List.of(names);
            ByteBuf buf = BufWorker.makeBufFromList(list, signal);
            ctx.writeAndFlush(buf);
        } else {
            System.out.println("Некорректный ввод данных. Введите: логин, пробел, пароль.");
        }
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService,
                         FileWorker fileWorker, byte signal) {
        List<String> list = BufWorker.readFileListFromBuf(buf);
        String login = list.get(0);
        String pass = list.get(1);
        int authResult = authService.getUserID(login, pass);
        if (authResult > -1) {
            AUTHUSER.sendToServer(ctx, String.valueOf(authResult));
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
