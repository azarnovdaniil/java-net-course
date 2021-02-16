package ru.daniilazarnov.common.files;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import ru.daniilazarnov.common.FilePackageConstants;
import ru.daniilazarnov.common.OperationTypes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSender {

    private final Channel channel;

    public FileSender(Channel channel) {
        this.channel = channel;
    }

    public void sendFile(Path path, ChannelFutureListener finishListener) throws IOException {

        FileRegion region = new DefaultFileRegion(path.toFile(), 0, Files.size(path));

        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(OperationTypes.UPLOAD.getCode());
        channel.writeAndFlush(buf);

        byte[] filenameBytes = path.getFileName().toString().getBytes(StandardCharsets.UTF_8);
        buf = ByteBufAllocator.DEFAULT.directBuffer(FilePackageConstants.NAME_LENGTH_BYTES.getCode());
        buf.writeInt(filenameBytes.length);
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(filenameBytes.length);
        buf.writeBytes(filenameBytes);
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(FilePackageConstants.FILE_LENGTH_BYTES.getCode());
        buf.writeLong(Files.size(path));
        channel.writeAndFlush(buf);

        ChannelFuture transferOperationFuture = channel.writeAndFlush(region);
        if (finishListener != null) {
            transferOperationFuture.addListener(finishListener);
        }
    }
}
