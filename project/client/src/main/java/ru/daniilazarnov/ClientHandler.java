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
        System.out.print("Enter command (enter /help for to get a list of commands): ");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            switch (scanner.nextLine()) {
                case "/list":
                    ctx.writeAndFlush(Command.LIST);
                    break;
                case "/help":
                    cf.getInfo();
                    break;
                case "/download":
                    System.out.print("Enter path to file: ");
                    String[] arg = {scanner.nextLine()};
                    Command download = Command.DOWNLOAD;
                    download.setArg(arg);
                    ctx.writeAndFlush(download);
            }
        }
    }
}
