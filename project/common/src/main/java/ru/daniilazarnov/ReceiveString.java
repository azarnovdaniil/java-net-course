package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;

public class ReceiveString {
    private static final int FOUR = 4;
    public static void sendString(String fileName,
                                  Channel channel, byte commandByte, ChannelFutureListener finishListener) {

        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(commandByte);
        channel.write(buf);
        buf = ByteBufAllocator.DEFAULT.directBuffer(FOUR);
        buf.writeInt(fileName.length());
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
