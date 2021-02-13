package ru.daniilazarnov.serialization;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.serialization.domain.FileMessage;
import ru.daniilazarnov.serialization.domain.MyMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CloudServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client connected...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Client disconnected...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println(msg.getClass().getName());
        if (msg instanceof MyMessage) {
            System.out.println("Client text message: " + ((MyMessage) msg).getText());
            ctx.writeAndFlush(new MyMessage("Hello Client!"));
        }
        if (msg instanceof FileMessage) {
            System.out.println("save file..");
            ctx.writeAndFlush(new MyMessage("Your file was succsefuly save"));
            try {
                Path path = Path.of(((FileMessage) msg).getFileName());
                Files.write(path, ((FileMessage) msg).getContent(), StandardOpenOption.CREATE_NEW);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.printf("Server received wrong object!");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
