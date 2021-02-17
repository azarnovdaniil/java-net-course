package ru.atoroschin.commands;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.atoroschin.AuthService;
import ru.atoroschin.BufWorker;

import java.util.List;

public class CommandAuthLoginPass implements CommandAuth{
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
    public int response(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService, byte signal) {
        List<String> list = BufWorker.readFileListFromBuf(buf);
        String login = list.get(0);
        String pass = list.get(1);
        return authService.getUserID(login, pass);
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService) {

    }
}
