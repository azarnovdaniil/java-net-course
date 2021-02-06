package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ServerFilesHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FileMessage) {
            FileMessage receivedFile = (FileMessage) msg;

            Path newFile = Paths.get("C:\\Users\\rav05\\OneDrive\\Desktop\\java_course_final\\java-net-course\\project\\server\\src\\main\\java\\ru\\daniilazarnov\\server_vault\\" + receivedFile.getFilename());
            Files.write(
                    newFile,
                    receivedFile.getData(),
                    StandardOpenOption.CREATE);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("hi");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("bye");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
