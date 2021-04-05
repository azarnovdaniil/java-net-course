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

    public static final String SERVER_REPO = "D:\\serverStorage";


    CommandServer cmd = new CommandServer();
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client connected...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Client disconnected...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String inCorrectRequest = "Wrong command!! See help for detailes /help";
        Common common = new Common();
        System.out.println(msg.getClass().getName());
        if (msg instanceof MyMessage) {
            System.out.println("Client text message: " + ((MyMessage) msg).getText());
            String s = ((MyMessage) msg).getText();
            String[] strings = s.split(" ");
                if (strings[0].equals("/help")){
                    s = cmd.callHelpManual().toString();
                }
                if (strings[0].equals("/show")){
                    s = cmd.showFiles(Path.of(SERVER_REPO)).toString();
                }
                else if (strings[0].equals("/upload")) {
                    if (strings.length != 2) {
                        s = inCorrectRequest;}
                }
                else if (strings[0].equals("/dowload") || strings.length == 2) {
                    Path senderFileAaddress = Path.of(SERVER_REPO + "\\" + strings[1]);
                    if (Files.exists(senderFileAaddress)) {
                        Future f = ctx.writeAndFlush(common.sendFile(strings[1], senderFileAaddress));
                    if (f.isDone()){
                        s = "The requested file was transferred";
                    }
                    } else {
                        s = "The specified file does not exist";
                    }
                }
                else if (strings[0].equals("/rename")){
                    if (strings.length == 3){
                    String address = SERVER_REPO + "\\" + strings[1];
                    String addressNew = SERVER_REPO + "\\" + strings[2];
                    cmd.renameFile(address, addressNew);
                    s = "File was renamed";
                    } else
                    { s = inCorrectRequest;}
                 }
                else if (strings[0].equals("/delete")){
                    if (strings.length == 2){
                    String address = SERVER_REPO + "\\" + strings[1];
                    cmd.deleteFile(address);
                    s = "File was deleted";
                    } else
                    { s = inCorrectRequest;}
                 }
                ctx.writeAndFlush(new MyMessage(s));
        } else if (msg instanceof FileMessage) {
        System.out.println("save file..");
        ctx.writeAndFlush(new MyMessage("Your file was succsefuly save"));
        common.receiveFile(msg,SERVER_REPO);
        }
        else {
            System.out.printf("Server received wrong object!");
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}