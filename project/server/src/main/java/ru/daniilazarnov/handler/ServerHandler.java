package ru.daniilazarnov.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.File;
import ru.daniilazarnov.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ServerHandler extends ChannelInboundHandlerAdapter {
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
        System.out.println("New " + msg.getClass().getSimpleName());
        if (msg instanceof Message) {
            System.out.println("Client message: " + ((Message) msg).getText());
        } else if (msg instanceof File) {
            System.out.println("save file...");
            ctx.writeAndFlush(new Message("Your file was successfully save"));
            try {
                Files.write(Path.of(((File) msg).getFileName()), ((File) msg).getContent(), StandardOpenOption.CREATE_NEW);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.print("Server received wrong object!\n");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
