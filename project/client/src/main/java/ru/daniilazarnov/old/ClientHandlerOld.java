package ru.daniilazarnov.old;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ClientHandlerOld extends ChannelInboundHandlerAdapter {

    private String currentDir = "D:\\testDir\\Client\\";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Connected to server...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof InfoMessageOld){
            System.out.println(((InfoMessageOld) msg).getMessage());
        }
        else if (msg instanceof CommandLSOld){
            for (String s : ((CommandLSOld) msg).listFiles()){
                System.out.println(s);
            }
        }
        else if (msg instanceof FileMessageOld) {
            System.out.println("Downloading the file..");
            ctx.writeAndFlush(new InfoMessageOld("Uploading the file..."));
            try {
                Files.write(Path.of(currentDir + ((FileMessageOld) msg).getFileName()), ((FileMessageOld) msg).getContent(), StandardOpenOption.CREATE_NEW);
                System.out.println("File saved successfully");
                ctx.writeAndFlush(new InfoMessageOld("File uploaded successfully"));
            } catch (IOException e) {
                System.out.println("SWW during saving the file");
                ctx.writeAndFlush(new InfoMessageOld("SWW during uploading the file"));
            }
        }
        else {
            System.out.print("Wrong object!");
            ctx.writeAndFlush(new InfoMessageOld("Wrong object!"));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Connection to server is lost");
        ctx.close();
    }
}
