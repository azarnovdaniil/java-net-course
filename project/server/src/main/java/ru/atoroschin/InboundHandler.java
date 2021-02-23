package ru.atoroschin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.atoroschin.auth.AuthService;

import java.util.HashMap;
import java.util.Map;

public class InboundHandler extends ChannelInboundHandlerAdapter {
    private Map<Integer, FileLoaded> uploadedFiles;
    private FileWorker fileWorker;
    private static final String STORAGE_DIR = "storage";
    private boolean auth = false;
    private final AuthService authService;

    public InboundHandler(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        System.out.println("Подключился клиент " + ctx.channel().remoteAddress().toString());
        int maxVolume = 1;
        uploadedFiles = new HashMap<>();
        fileWorker = new FileWorker(STORAGE_DIR, STORAGE_DIR, maxVolume);
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;

        if (buf.readableBytes() > 0) {
            byte b = buf.readByte();
            if (!auth) {
                CommandsAuth command = CommandsAuth.getCommand(b);
                if (command.equals(CommandsAuth.AUTHUSER)) {
                    try {
                        command.receiveAndSend(ctx, buf, authService, fileWorker);
                        auth = true;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Commands command = Commands.getCommand(b);
                command.receiveAndSend(ctx, buf, fileWorker, uploadedFiles);
            }
        }
        buf.release();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Отключился клиент " + ctx.channel().remoteAddress().toString());
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
