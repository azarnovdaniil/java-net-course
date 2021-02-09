package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

public class SendFileName {

    public static void sendFileName(String fileName, Channel channel, ChannelFutureListener finishListener){

        ByteBuf  buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(1); //управляющий байт
        channel.write(buf);
        buf = ByteBufAllocator.DEFAULT.directBuffer(4);
        buf.writeInt(fileName.length()); // длинна имени файла
        channel.write(buf);
        byte[] stringSource = fileName.getBytes();
        buf = ByteBufAllocator.DEFAULT.directBuffer(fileName.length());
        buf.writeBytes(stringSource);
        channel.write(buf);
        channel.flush();
//        buf.release(); //на этой строке рвет соединнение с сервером

    }


}
