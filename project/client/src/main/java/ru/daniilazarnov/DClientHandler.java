package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.commands.Commands;
import java.io.InputStream;
import java.util.Scanner;

public class DClientHandler extends ChannelInboundHandlerAdapter {
    User user;
    MessagePacket mP;
    Scanner scanner = new Scanner(System.in);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("The client is connected to the server!\n");
        PrintMessages hello = new PrintMessages();
        String fileName = "MessageHello.txt";
        InputStream is = hello.getFileFromResourceAsStream(fileName);
        hello.printInputStream(is);
        user = new User(hello.getUserName(), hello.getSecretKey());
        mP = new MessagePacket(user.getClientName(), user.getClientKEY());
        PrintMessages help = new PrintMessages();
        fileName = "MessageAboutCommands.txt";
        is = help.getFileFromResourceAsStream(fileName);
        help.printInputStream(is);
        user.setHomeFolder(user.getClientName().toLowerCase());
        ctx.channel().writeAndFlush(user.invoke(scanner, mP));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Client disconnected.");
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof MessagePacket) {
            MessagePacket mp = (MessagePacket) msg;
            Commands commands = mp.getCommand();
            MessagePacket answer = commands.runClientCommands(mp);
        }
        ctx.channel().writeAndFlush(user.invoke(scanner, mP));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
