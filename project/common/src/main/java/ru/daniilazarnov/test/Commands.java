package ru.daniilazarnov.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Commands {

    public void listFiles(ChannelHandlerContext ctx, String directory) throws IOException {
        File dir = new File(directory);
        File[] files = dir.listFiles();
        ArrayList<String> list = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    list.add(String.format("[%s] size: %s", file.getName(), Utils.bytesConverter(file.length())));
                }
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(list);
        byte[] bytes = bos.toByteArray();
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        buf.writeByte((byte) 45);
        buf.writeBytes(Utils.convertToByteBuf(bytes));
        ctx.writeAndFlush(buf);
    }
}
