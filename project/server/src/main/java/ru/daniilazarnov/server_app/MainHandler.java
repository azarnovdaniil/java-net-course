package ru.daniilazarnov.server_app;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.Command;


import java.nio.charset.StandardCharsets;

import static ru.daniilazarnov.Command.*;


public class MainHandler extends ChannelInboundHandlerAdapter {
    final private ServerApp serverApp;
    ClientHandler clientHandler;
    Commands commands;


    public MainHandler(ServerApp serverApp, ClientHandler clientHandler) {
        this.serverApp=serverApp;
        this.clientHandler = clientHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf mes = ctx.alloc().buffer();
        ctx.writeAndFlush(mes.writeBytes("Для помощи введите -help".getBytes(StandardCharsets.UTF_8)));
        commands = new Commands();
        System.out.println("Подключился клиент: " + ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buff = (ByteBuf)msg;
        if (commands.getCurrentState() == Commands.State.FILE){
            commands.toUpLoad(clientHandler, buff,ctx);
        }
        else{
        Command command = Command.getCommand(buff.readByte());
        switch (command) {
            case AUTH:
                commands.toLogIn(buff, serverApp, clientHandler, ctx);
                break;
            case REG:
                commands.toRegister(buff, serverApp, ctx);
                break;
            case LS:
                commands.toLS(clientHandler, ctx);
                break;
            case MKDIR:
                commands.toMkDir(buff, clientHandler, ctx);
                break;
            case HELP:
                commands.toShowHelp(ctx);
                break;
            case CD:
                commands.toCD(buff, clientHandler, ctx);
                break;
            case UPLOAD:
                commands.toUpLoad(clientHandler,buff,ctx);
                break;
            case DOWNLOAD:
                commands.toDownload(clientHandler,buff,ctx);
                break;
            case RM:
                commands.toRemove(clientHandler,buff,ctx);
            case RN:
                commands.toRename(clientHandler,buff,ctx);
            case UNKNOWN:
                break;
        }
        }
        if (buff.readableBytes() == 0) {
            buff.release();
        }

    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Отключился клиент " + clientHandler.getName(ctx.channel()));
        clientHandler.unsubscribe(ctx.channel());
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
