package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Files;

@Deprecated
public class SendCommandByte {

    public static void sendByte(byte b, Channel channel, ChannelFutureListener finishListener){
        ByteBuf  buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(b);
        channel.write(buf);
    }

}
