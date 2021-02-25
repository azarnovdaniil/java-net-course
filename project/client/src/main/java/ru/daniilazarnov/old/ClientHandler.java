package ru.daniilazarnov.old;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.old.CommandLS;
import ru.daniilazarnov.old.FileMessage;
import ru.daniilazarnov.old.InfoMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private String currentDir = "D:\\testDir\\Client\\";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Connected to server...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof InfoMessage){
            System.out.println(((InfoMessage) msg).getMessage());
        }
        else if (msg instanceof CommandLS){
            for (String s : ((CommandLS) msg).listFiles()){
                System.out.println(s);
            }
        }
        else if (msg instanceof FileMessage) {
            System.out.println("Downloading the file..");
            ctx.writeAndFlush(new InfoMessage("Uploading the file..."));
            try {
                Files.write(Path.of(currentDir + ((FileMessage) msg).getFileName()), ((FileMessage) msg).getContent(), StandardOpenOption.CREATE_NEW);
                System.out.println("File saved successfully");
                ctx.writeAndFlush(new InfoMessage("File uploaded successfully"));
            } catch (IOException e) {
                System.out.println("SWW during saving the file");
                ctx.writeAndFlush(new InfoMessage("SWW during uploading the file"));
            }
        }
        else {
            System.out.print("Wrong object!");
            ctx.writeAndFlush(new InfoMessage("Wrong object!"));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Connection to server is lost");
        ctx.close();
    }
}
