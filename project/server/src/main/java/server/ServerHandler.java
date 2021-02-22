package server;

import delete.Delete;
import download.DownLoad;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import org.checkerframework.checker.units.qual.A;
import ru.daniilazarnov.FileMessage;
import ru.daniilazarnov.FileRequest;
import ru.daniilazarnov.MyMessage;
import upload.UpLoad;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final String WAY_SERVER = ("project/server/src/main/java/file/");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился ");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Клиент отключился");
    }

    @Override
    public void channelRead(io.netty.channel.ChannelHandlerContext ctx, java.lang.Object msg)
            throws java.lang.Exception {


        // Отправка пакета на клиент

        if (msg instanceof FileRequest) {
            String message = String.valueOf(msg);
            System.out.println("message");
            DownLoad.upLoad(ctx, msg);
        }

        // Прием пакета от клиента

        if (msg instanceof FileMessage) {
            UpLoad.download(msg);
        }

        //вывод всех файлов

        if (msg instanceof String) {
            String message = (String) msg;
            String[] messageCommand = message.split("\\s");

            if (messageCommand[0].equals("/удалить")) {

                Delete.delete(message, messageCommand, ctx, msg);

            }
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


    public void search(io.netty.channel.ChannelHandlerContext ctx, java.lang.String files) {
        Path path = Paths.get("project", "server", "src", "main", "java", "file", files);
        ctx.writeAndFlush(Files.exists(path));
    }


}
