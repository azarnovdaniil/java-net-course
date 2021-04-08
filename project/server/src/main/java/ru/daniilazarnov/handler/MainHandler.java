package ru.daniilazarnov.handler;

import io.netty.util.concurrent.Future;
import ru.daniilazarnov.Server;
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
        String addressUser = Server.SERVER_REPO;
        MyMessage message = new MyMessage("");
        CommandServer cmd;
        String inCorrectRequest = "Wrong command!! See help for detailes /help";
        System.out.println(ctx.channel().remoteAddress());
        Common common = new Common();
        if (msg instanceof MyMessage) {
            System.out.println("Client text message: " + ((MyMessage) msg).getText());
            String s = ((MyMessage) msg).getText();
            String[] strings = s.split(" ");
                if (strings[0].equals("/help")) {
                    ctx.writeAndFlush(new MyMessage((new CommandServer()).callHelpManual().toString()));
                } else if (strings[0].equals("/show")) {
                    ctx.writeAndFlush(new MyMessage((new Common()).showFiles(addressUser).toString()));
                } else if (strings[0].equals("/upload")) {
                    if (strings.length != 2) {
                        s = inCorrectRequest;
                    }
                } else if (strings[0].equals("/dowload") || strings.length == 2) {
                    Path senderFileAaddress = Path.of(addressUser + "\\" + strings[1]);
                    if (Files.exists(senderFileAaddress)) {
                        Future f = ctx.writeAndFlush(common.sendFile(strings[1], senderFileAaddress));
                    if (f.isDone()) {
                        s = "The requested file was transferred";
                    }
                    } else {
                        s = "The specified file does not exist";
                    }
                    } else if (strings[0].equals("/rename")) {
                    if (strings.length == maxLengthArrayRename) {
                        ctx.writeAndFlush(new MyMessage(common.renameFile(strings[1], strings[2], addressUser)));
                    } else {
                        s = inCorrectRequest;
                    }
                 } else if (strings[0].equals("/delete")) {
                    if (strings.length == 2) {
                        ctx.writeAndFlush(new MyMessage((common.deleteFile(strings[1], addressUser))));
                    } else {
                        s = inCorrectRequest;
                    }
                } else {
                    ctx.writeAndFlush(new MyMessage("Wrong command"));
                }
        } else if (msg instanceof FileMessage) {
        System.out.println("save file..");
        ctx.writeAndFlush(new MyMessage("Your file was succsefuly save"));
        common.receiveFile(msg, addressUser);
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
