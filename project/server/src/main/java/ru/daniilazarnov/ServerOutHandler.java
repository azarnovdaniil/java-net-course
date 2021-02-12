package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Scanner;

public class ServerOutHandler extends ChannelInboundHandlerAdapter {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Thread t1 = new Thread(() -> {
            System.out.println("Client connected...");
            String s = "Добро пожаловать в облачное хранилище!" + System.lineSeparator()
                    + "Вам доступны следующие команды:" + System.lineSeparator()
                    + "*send - отправить файл в хранилище" + System.lineSeparator()
                    + "*dl  - получить файл из хранилища" + System.lineSeparator()
                    + "*exit - выход";
            ctx.writeAndFlush(s);
            while (true){
                ctx.writeAndFlush(scanner.nextLine());
            }
        });
        t1.start();
    }
}
