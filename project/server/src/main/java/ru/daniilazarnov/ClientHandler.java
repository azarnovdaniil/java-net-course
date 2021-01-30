package ru.daniilazarnov;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.nio.file.Path;
import java.nio.file.Paths;


public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился ");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Клиент отключился");
    }

    @Override
    public void channelRead(io.netty.channel.ChannelHandlerContext ctx, java.lang.Object msg) throws java.lang.Exception {


        System.out.println(msg.getClass().getName());


            if (msg instanceof MyMessage){
                if ( msg.equals("/all")) {
                    Path path = Paths.get("project", "server", "src", "main", "java", "ru.daniilazarnov", "file");

                    ctx.writeAndFlush(new File(path.getFileName().toString()));
                }
            }




    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
