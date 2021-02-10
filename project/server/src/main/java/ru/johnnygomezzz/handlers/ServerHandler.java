package ru.johnnygomezzz.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.johnnygomezzz.FileMessage;
import ru.johnnygomezzz.MyMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private MyMessage textMessage;

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("Клиент подключился");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Клиент отключился");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){

        if (msg instanceof MyMessage) {
            System.out.println("Сообщение от клиента: " + ((MyMessage) msg).getText());
            if (((MyMessage) msg).getText().equals("/quit")) {
                textMessage = new MyMessage("/quit");
                ctx.writeAndFlush(textMessage);
                System.exit(0);
            }
        //} else if (msg instanceof FileMessage) {
        //    System.out.println("save file..");
        //    ctx.writeAndFlush(new MyMessage("Your file was successfully saved"));
        //    try {
        //        Files.write(Path.of(((FileMessage) msg).getFileName()), ((FileMessage) msg).getContent(), StandardOpenOption.CREATE_NEW);
        //    } catch (IOException e) {
        //        e.printStackTrace();
        //    }
        } else {
            System.out.println("Server received wrong object!");
        }
        Scanner scanner = new Scanner(System.in);
        String srvMsg = scanner.nextLine();

        textMessage = new MyMessage(srvMsg);
        ctx.writeAndFlush(textMessage);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
