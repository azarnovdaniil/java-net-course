package exercise;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import exercise.domain.FileMessageEx;
import exercise.domain.MyMessageEx;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CloudServerHandlerEx extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client connected...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Client disconnected...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println(msg.getClass().getName());
        if (msg instanceof MyMessageEx) {
            System.out.println("Client text message: " + ((MyMessageEx) msg).getText());
            ctx.writeAndFlush(new MyMessageEx("Hello Client!"));
        }
        if (msg instanceof FileMessageEx) {
            System.out.println("save file..");
            ctx.writeAndFlush(new MyMessageEx("Your file was succsefuly save"));
            try {
                Path path = Path.of(((FileMessageEx) msg).getFileName());
                Files.write(path, ((FileMessageEx) msg).getContent(), StandardOpenOption.CREATE_NEW);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
