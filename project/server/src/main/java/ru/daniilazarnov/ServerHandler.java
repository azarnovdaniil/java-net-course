package ru.daniilazarnov;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private Channel channel;
    private CommandController controller;
    private String currentDir = "D:\\testDir\\Server\\";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
        controller = new CommandController(channel);
        System.out.println("Client connected...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof InfoMessage){
            System.out.println(((InfoMessage) msg).getMessage());
        }
        else if (msg instanceof FileMessage) {
            System.out.println("Saving the file..");
            ctx.writeAndFlush(new InfoMessage("Uploading the file..."));
            try {
                Files.write(Path.of(currentDir + ((FileMessage) msg).getFileName()), ((FileMessage) msg).getContent(), StandardOpenOption.CREATE_NEW);
                System.out.println("File saved successfully");
                ctx.writeAndFlush(new InfoMessage("File uploaded"));
            } catch (IOException e) {
                System.out.println("SWW during saving the file");
                ctx.writeAndFlush(new InfoMessage("SWW during uploading the file"));
            }
        }
        else if (msg instanceof FileRequestMessage) {
            String fileName = ((FileRequestMessage) msg).getRequest();
            String[] out = new String[3];
            out[0] = "upl";
            out[1] = currentDir;
            out[2] = fileName;
            controller.command(out);
        }
        else if (msg instanceof RequestLS){
            ctx.writeAndFlush(new CommandLS(currentDir));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Client disconnected");
        channel.close();
    }
}
