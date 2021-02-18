package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;


public class ClientHandlerNetty extends ChannelInboundHandlerAdapter {
    private static final Logger log = Logger.getLogger(ClientHandlerNetty.class);
    private Map<Integer, FileLoaded> uploadedFiles;
    private FileWorker fileWorker;
    private boolean active;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        uploadedFiles = new HashMap<>();
        fileWorker = new FileWorker("client", "");
        ctx.fireChannelRegistered();
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        active = true;
        try {
            consoleRead(ctx);
        } catch (IOException e) {

        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        if (buf.readableBytes() > 0) {
            byte firstByte = buf.readByte();
            Commands command = Commands.getCommand(firstByte);
            command.receive(ctx, buf, fileWorker, uploadedFiles);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause);
        active = false;
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        active = false;
        super.channelInactive(ctx);
    }

    public void consoleRead(ChannelHandlerContext ctx) throws IOException {

        Thread threadConsole = new Thread(() -> {
            while (active) {
                String inputLine;
                InputStream in = System.in;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                try {
                    inputLine = bufferedReader.readLine().trim().toLowerCase();
                    String[] firstCommand = inputLine.split("\\s", 2);

                    System.out.print(fileWorker.getCurrentDir() + ">");

                    Commands commands;
                    try {
                        commands = Commands.valueOf(firstCommand[0].toUpperCase(Locale.ROOT));
                    } catch (IllegalArgumentException ex) {
                        commands = Commands.UNKNOWN;
                    }
                    String commandParam = firstCommand.length > 1 ? firstCommand[1] : firstCommand[0];

                    commands.sendToServer(ctx, commandParam, fileWorker);
                } catch (IOException e) {
                    log.error(e);
                }
            }
        });
        threadConsole.setDaemon(true);
        threadConsole.start();
    }
}