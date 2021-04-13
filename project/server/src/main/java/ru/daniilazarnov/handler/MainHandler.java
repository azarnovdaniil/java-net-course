package ru.daniilazarnov.handler;

import io.netty.util.concurrent.Future;

import ru.daniilazarnov.domain.FileMessage;
import ru.daniilazarnov.domain.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.Common;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainHandler extends ChannelInboundHandlerAdapter { // (1)
private final int maxLengthArrayRename = 3;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String address = Common.readConfig().getServerRepo() + "\\" + UserPool.getUserName(ctx.channel());
        String inCorrectRequest = "Wrong command!! See help for details /help";

        if (msg instanceof MyMessage) {
            System.out.println("Client text message: " + ((MyMessage) msg).getText());
            String s = ((MyMessage) msg).getText();
            String[] strings = s.split(" ");
                if (strings[0].equals("/help")) {
                    ctx.writeAndFlush(new MyMessage((new CommandServer()).callHelpManual().toString()));
                } else if (strings[0].equals("/show")) {
                    ctx.writeAndFlush(new MyMessage((new Common()).showFiles(address).toString()));
                } else if (strings[0].equals("/upload")) {
                    if (strings.length != 2) {
                        s = inCorrectRequest;
                    }
                } else if (strings[0].equals("/download") || strings.length == 2) {
                    Path senderFileAddress = Path.of(address + "\\" + strings[1]);
                    if (Files.exists(senderFileAddress)) {
                        Future f = ctx.writeAndFlush((new Common()).sendFile(strings[1], senderFileAddress));
                    if (f.isDone()) {
                        s = "The requested file was transferred";
                    }
                    } else {
                        s = "The specified file does not exist";
                    }
                    } else if (strings[0].equals("/rename")) {
                    if (strings.length == maxLengthArrayRename) {
                        ctx.writeAndFlush(new MyMessage((new Common()).renameFile(strings[1], strings[2], address)));
                    } else {
                        s = inCorrectRequest;
                    }
                 } else if (strings[0].equals("/delete")) {
                    if (strings.length == 2) {
                        ctx.writeAndFlush(new MyMessage((new Common().deleteFile(strings[1], address))));
                    } else {
                        s = inCorrectRequest;
                    }
                } else {
                    ctx.writeAndFlush(new MyMessage("Wrong command"));
                }
        } else if (msg instanceof FileMessage) {
        System.out.println("save file..");
        ctx.writeAndFlush(new MyMessage("Your file was successfully save"));
        (new Common()).receiveFile(msg, address);
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
