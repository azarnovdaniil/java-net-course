package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

import java.io.File;


public class OutHandler extends ChannelOutboundHandlerAdapter {

    private String path = "project/server/serverFiles";

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof String) {
            System.out.println("Out string - " + msg);
            String str = (String) msg;
            if (str.startsWith("download")) {
                String fileName = str.replace("download: ", "");
                File file = new File(path + "/" + fileName);
                long size = file.length();
                ByteBuf buf = ctx.alloc().buffer();
                buf.writeLong(size);
                ctx.writeAndFlush(buf);
                System.out.println("STATE: Sent downloading file length - " + size + " bytes");
                FileRegion region = new DefaultFileRegion(file, 0, size);
                ctx.writeAndFlush(region);
            } else {
                byte[] arr = (str).getBytes();
                // alloc() - ссылка на базовый Аллокатор, который выделяет память для буфера
                ByteBuf buf = ctx.alloc().buffer(arr.length);
                buf.writeBytes(arr);
                ctx.writeAndFlush(buf);
            }
        }

        if (msg instanceof Integer) {
            System.out.println("Out Int - " + msg);
            int i = (int) msg;
            ByteBuf buf = ctx.alloc().buffer(); // по сути объявление буфера
            buf.writeInt(i);
            ctx.writeAndFlush(buf);
        }

        if (msg instanceof Byte) {
            System.out.println("Out Byte - " + msg);
            byte b = (byte) msg;
            ByteBuf buf = ctx.alloc().buffer(); // по сути объявление буфера
            buf.writeByte(b);
            ctx.writeAndFlush(buf);
        }
    }
}
