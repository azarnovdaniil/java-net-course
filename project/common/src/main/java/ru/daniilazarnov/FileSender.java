package common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileSender {

    private static boolean loadingStatus = false;
    private static final int FOUR = 4;
    private static final int EIGHTS = 8;

    public static void setLoadingStatus(boolean loadingStatus) {
        FileSender.loadingStatus = loadingStatus;
    }

    public static boolean isLoadingStatus() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return loadingStatus;
    }

    public static void sendFile(Path path, Channel channel, ChannelFutureListener finishListener) throws IOException {
        FileRegion region = new DefaultFileRegion(new FileInputStream(path.toFile()).getChannel(), 0, Files.size(path));

        ByteBuf buf;
        buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte((byte) 2);
        channel.write(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(FOUR);
        buf.writeInt(path.getFileName().toString().length());
        channel.write(buf);

        byte[] filenameBytes = path.getFileName().toString().getBytes();
        buf = ByteBufAllocator.DEFAULT.directBuffer(filenameBytes.length);
        buf.writeBytes(filenameBytes);
        channel.write(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(EIGHTS);
        buf.writeLong(Files.size(path));
        channel.write(buf);

        ChannelFuture transferOperationFuture = channel.writeAndFlush(region);
        if (finishListener != null) {
            transferOperationFuture.addListener(finishListener);
        }

    }


}
