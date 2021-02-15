package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Scanner;

public class ClientOutHandler extends ChannelInboundHandlerAdapter {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Thread t1 = new Thread(() -> {
            System.out.println("Client connected...");
            while (true){
                ctx.writeAndFlush(scanner.next());
            }
        });
        t1.start();
    }
}
