package ru.daniilazarnov;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainHandler extends SimpleChannelInboundHandler<String> {
    private static final List<Channel> channels = new ArrayList<>();
    private static int clientId = 1;
    private String clientName;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected!" + ctx);
        channels.add(ctx.channel());
        clientName = "Client #" + clientId;
        clientId++;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Charset charset = StandardCharsets.UTF_16;
        byte[] arr = msg.getBytes(charset);

        File clientDir = new File("D:/testDir/" + clientName);


        System.out.println("Message recorded into the file");
//        ctx.writeAndFlush("ctx:" + msg);

        System.out.println(String.format("Received message from [%s]: %s", clientName, msg));

        if (msg.startsWith("/")){
            if (msg.startsWith("/chname ")) {
                clientName = msg.split("\\s", 2)[1];
            } else if (msg.equals("/ls")){
                listFiles(ctx);
            } else if (msg.startsWith("/upload ")){
                if (!clientDir.exists()){
                    clientDir.mkdirs();
                }
                Files.write(Paths.get("D:/testDir/"+ clientName +"/" + msg.split("\\s", 2)[1] + ".txt"), arr);
            }
            return;
        }
        String out = String.format("[%s]: %s", clientName, msg);
        for (Channel ch : channels){
            ch.writeAndFlush(out);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(String.format("Client [%s] disconnected", clientName));
        channels.remove(ctx.channel());
        ctx.close();
    }

    public void listFiles(ChannelHandlerContext ctx) {
        String[] pathnames;
        File f = new File("D:/testDir/" + clientName);
        pathnames = f.list();
        for (String pathname : pathnames) {
            ctx.writeAndFlush(pathname + "\n");
        }
    }
}
