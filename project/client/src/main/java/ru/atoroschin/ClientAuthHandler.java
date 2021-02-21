package ru.atoroschin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientAuthHandler extends ChannelInboundHandlerAdapter {
    private boolean auth;
    private final String fileCredentials = "prop.crd";

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        auth = false;
        readAuth(channelHandlerContext, new FileReadCredentials(fileCredentials));
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        if (buf.readableBytes() > 0) {
            byte firstByte = buf.readByte();
//            if (!auth) {
                CommandsAuth command = CommandsAuth.getCommand(firstByte);
                command.receive(channelHandlerContext, buf, null);
                if (command.equals(CommandsAuth.AUTHOK)) {
                    auth = true;
                    channelHandlerContext.channel().pipeline().remove(this);
                    channelHandlerContext.fireChannelActive();
                } else {
                    System.out.println("Неверный логин или пароль");
                }
//            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        cause.printStackTrace();
        channelHandlerContext.close();
    }

    public void readAuth(ChannelHandlerContext ctx, ReadCredentials readCredentials) {
//        Thread threadConsoleAuth = new Thread(() -> {
        
//            while (!auth) {
                try {
                    Credentials credentials = readCredentials.read();

                    CommandsAuth command = CommandsAuth.AUTH;
                    command.sendToServer(ctx, credentials);
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
//            }
//        });
//        threadConsoleAuth.setDaemon(true);
//        threadConsoleAuth.start();
    }

//    public void consoleReadAuth(ChannelHandlerContext ctx) {
//        Thread threadConsoleAuth = new Thread(() -> {
//            Scanner scanner = new Scanner(System.in);
//            while (!auth) {
//                String readLine;
//                System.out.print("Введите логин и пароль: ");
//                if (scanner.hasNext()) {
//                    readLine = scanner.nextLine();
//                    CommandsAuth command = CommandsAuth.AUTH;
//                    command.sendToServer(ctx, readLine);
//                    try {
//                        TimeUnit.SECONDS.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        threadConsoleAuth.setDaemon(true);
//        threadConsoleAuth.start();
//    }
}
