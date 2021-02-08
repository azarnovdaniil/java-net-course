package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;

import java.util.Scanner;

public class ClientFunctional {
    protected void getInfo(){
        System.out.println("Command list: ");
        for (Command cmd : Command.values()){
            System.out.println("\t" + cmd.getCommand() + cmd.getDescription());
        }
    }
    protected void downloadFile(ChannelHandlerContext ctx, Scanner scanner) {
        System.out.print("Enter path to file: ");
        String[] arg = {scanner.nextLine()};
        Command download = Command.DOWNLOAD;
        download.setArg(arg);
        ctx.writeAndFlush(download);
    }

    protected void uploadFile(ChannelHandlerContext ctx, Scanner scanner){

    }

    protected void removeFile(ChannelHandlerContext ctx, Scanner scanner){

    }

    protected void moveFile(ChannelHandlerContext ctx, Scanner scanner){

    }
}
