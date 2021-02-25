package ru.daniilazarnov.common.commands.download;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import ru.daniilazarnov.common.commands.Command;
import ru.daniilazarnov.common.CommonPackageConstants;
import ru.daniilazarnov.common.commands.Commands;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DownloadCommand implements Command {
    private Channel channel;
    private String stringPath;

    public DownloadCommand(Channel channel, String stringPath) {
        this.channel = channel;
        this.stringPath = stringPath;
    }


    private void sendDownloadCommand(ChannelFutureListener finishListener) throws IOException {
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(Commands.DOWNLOAD.getCode());
        channel.writeAndFlush(buf);

        byte[] filePath = stringPath.getBytes(StandardCharsets.UTF_8);
        buf = ByteBufAllocator.DEFAULT.directBuffer(CommonPackageConstants.CONTENT_LENGTH_BYTES.getCode());
        buf.writeInt(filePath.length);
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(filePath.length);
        buf.writeBytes(filePath);
        ChannelFuture transferOperationFuture = channel.writeAndFlush(buf);
        if (finishListener != null) {
            transferOperationFuture.addListener(finishListener);
        }


    }

    @Override
    public void execute() {
        try {
            sendDownloadCommand(
                    future -> {
                        if (!future.isSuccess()) {
                            future.cause().printStackTrace();
                        }
                        if (future.isSuccess()) {
                            System.out.println("Download command has been sent");
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
