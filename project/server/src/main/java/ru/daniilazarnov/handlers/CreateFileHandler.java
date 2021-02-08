package ru.daniilazarnov.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class CreateFileHandler extends ChannelInboundHandlerAdapter {

    private FileWriter writer;

    @Override
    public void channelRead (ChannelHandlerContext ctx, Object msg) {
        System.out.println("Записать файл например");
        System.out.println("Second");
        ByteBuf buf = (ByteBuf) msg;
        String a = (String) msg;
        createFromConsole();
        FileWriter try {
            writer = new FileWriter("src/main/java/ru/daniilazarnov/exampleFiles/file.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.write((String) msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


        byte[] data = new byte[3];
        buf.readBytes(data);
        buf.release();
        System.out.println(Arrays.toString(data));
        ctx.fireChannelRead(data);

}





    public void createFromConsole() {
        File file = new File("src/main/java/ru/daniilazarnov/exampleFiles/file.txt");
        assertFalse(file.exists());


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
