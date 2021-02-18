package ru.atoroschin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientHandlerNetty extends ChannelInboundHandlerAdapter {
    private Map<Integer, FileLoaded> uploadedFiles;
    private FileWorker fileWorker;
    private boolean active;
    private boolean auth;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        uploadedFiles = new HashMap<>();
        auth = false;
        fileWorker = new FileWorker("client", "", -1);
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        active = true;
        consoleReadAuth(channelHandlerContext);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        if (buf.readableBytes() > 0) {
            byte firstByte = buf.readByte();
            if (!auth) {
                CommandsAuth command = CommandsAuth.getCommand(firstByte);
                if (command.equals(CommandsAuth.AUTHOK)) {
                    auth = true;
                    consoleRead(channelHandlerContext);
                } else {
                    System.out.println("Неверный логин или пароль");
                }
            } else {
                Commands command = Commands.getCommand(firstByte);
                command.receive(channelHandlerContext, buf, fileWorker, uploadedFiles);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        cause.printStackTrace();
        active = false;
        channelHandlerContext.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        active = false;
        super.channelInactive(ctx);
    }

    public void consoleRead(ChannelHandlerContext ctx) {
        Thread threadConsole = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (active) {
                String readLine;
                System.out.println("server: " + fileWorker.getServerPathOnClient() + ">");
                System.out.print("local: " + fileWorker.getCurrentDir() + ">");
                if (scanner.hasNext()) {
                    readLine = scanner.nextLine();
                    String[] commandPart = readLine.split("\\s", 2);
                    Commands command;
                    try {
                        command = Commands.valueOf(commandPart[0].toUpperCase(Locale.ROOT));
                    } catch (IllegalArgumentException ex) {
                        command = Commands.UNKNOWN;
                    }
                    String commandParam = commandPart.length > 1 ? commandPart[1] : commandPart[0];

                    command.sendToServer(ctx, commandParam, fileWorker);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadConsole.setDaemon(true);
        threadConsole.start();
    }

    public void consoleReadAuth(ChannelHandlerContext ctx) {
        Thread threadConsoleAuth = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (!auth) {
                String readLine;
                System.out.print("Введите логин и пароль: ");
                if (scanner.hasNext()) {
                    readLine = scanner.nextLine();
                    CommandsAuth command = CommandsAuth.AUTH;
                    command.sendToServer(ctx, readLine);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadConsoleAuth.setDaemon(true);
        threadConsoleAuth.start();
    }
}
