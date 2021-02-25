package ru.daniilazarnov.common.messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import ru.daniilazarnov.common.CommonPackageConstants;
import ru.daniilazarnov.common.commands.Commands;

import java.nio.charset.StandardCharsets;


public class TextMessageSender {

    private final Channel channel;

    public TextMessageSender(Channel channel) {
        this.channel = channel;
    }

    public void sendMessage(String message) {

        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(Commands.MESSAGE.getCode());
        channel.writeAndFlush(buf);

        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        buf = ByteBufAllocator.DEFAULT.directBuffer(CommonPackageConstants.CONTENT_LENGTH_BYTES.getCode());
        buf.writeInt(messageBytes.length);
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(messageBytes.length);
        buf.writeBytes(messageBytes);
        channel.writeAndFlush(buf);

    }


}
