package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

/**
 * Класс содержит логику отправления побайтово имени файла на сервер
 */
public class ReceivingAndSendingStrings {
    private static final int FOUR = 4;

    /**
     * Формирует для отправки на сервер имя файла по протоколу
     * [] byte - управляющий байт;
     * [][][][] int  = длинна имени файла;
     * [] byte[] - имя файла;
     *
     * @param fileName - имя файла;
     * @param channel  - канал для передачи;
     */
    public static void sendString(String fileName,
                                  Channel channel, byte commandByte, ChannelFutureListener finishListener) {

        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(commandByte); //управляющий байт
        channel.write(buf);
        buf = ByteBufAllocator.DEFAULT.directBuffer(FOUR);
        buf.writeInt(fileName.length()); // длинна имени файла
        channel.write(buf);
        byte[] stringSource = fileName.getBytes();
        buf = ByteBufAllocator.DEFAULT.directBuffer(fileName.length());
        buf.writeBytes(stringSource);
        channel.write(buf);
        channel.flush();
    }

    /**
     * Принимает строку по протоколу   * Формирует для отправки на сервер имя файла по протоколу
     * * [][][][] int  = длинна имени файла;
     * * [] byte[] - имя файла;
     */
    public static String receiveAndEncodeString(ByteBuf buf) {
        int msgLength = (byte) buf.readInt();
        byte[] messageContent = new byte[msgLength];
        buf.readBytes(messageContent);
        return new String(messageContent);
    }
}
