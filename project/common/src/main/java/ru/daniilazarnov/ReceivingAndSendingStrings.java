package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

public class ReceivingAndSendingStrings {

    public static void sendString(String fileName, Channel channel, byte commandByte,
                                  ChannelFutureListener finishListener) {

        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(commandByte);
        channel.write(buf);
        buf = ByteBufAllocator.DEFAULT.directBuffer(4);
        buf.writeInt(fileName.length()); // длинна имени файла
        channel.write(buf);
        byte[] stringSource = fileName.getBytes();
        buf = ByteBufAllocator.DEFAULT.directBuffer(fileName.length());
        buf.writeBytes(stringSource);
        channel.write(buf);
        channel.flush();
    }

    public static String receiveAndEncodeString(ByteBuf buf) {
        int msgLength = (byte) buf.readInt();
        byte[] messageContent = new byte[msgLength];
        buf.readBytes(messageContent);
        return new String(messageContent);
    }
}
