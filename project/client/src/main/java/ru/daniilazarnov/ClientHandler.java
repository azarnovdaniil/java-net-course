package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;
import java.util.Scanner;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private ClientFunctional cf = new ClientFunctional();
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sendCommand(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof String[]){
            String[] list = (String[]) msg;
            for (String o: list){
                System.out.println(o);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(Arrays.toString(cause.getStackTrace()));
    }

    private void sendCommand(ChannelHandlerContext ctx){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter command (enter /help for to get a list of commands): ");
            switch (scanner.nextLine()) {
                case "/list":
                    ctx.writeAndFlush(Command.LIST);
                    break;
                case "/help":
                    cf.getInfo();
                    break;
                case "/download":
                    cf.downloadFile(ctx, scanner);
                    break;
                case "/upload":
                    cf.uploadFile(ctx, scanner);
                    break;
                case "/remove":
                    cf.removeFile(ctx, scanner);
                    break;
                case "/move":
                    cf.moveFile(ctx, scanner);
                    break;
                case "/exit":
                    ctx.writeAndFlush(Command.EXIT);
                    break;
                default:
                    System.out.println("Entered incorrect command, please, try again");
                    break;
            }
        }
    }


}
