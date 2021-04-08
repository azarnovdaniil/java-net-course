package ru.daniilazarnov.proto.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

public class ProtoClient {

    public static final byte MAGIC_BYTE = (byte) 25;
    private final Network network;

    public ProtoClient(CountDownLatch networkStarter) {
        this.network = new Network();
        network.start(networkStarter);
    }

    public static void main(String[] args) throws Exception {
        CountDownLatch networkStarter = new CountDownLatch(1);
        ProtoClient protoClient = new ProtoClient(networkStarter);

        protoClient.run(Paths.get("demo.txt"));
    }

    public void run(Path path) throws IOException {
        sendFile(path, future -> {
            if (!future.isSuccess()) {
                future.cause().printStackTrace();
                network.stop();
            }
            if (future.isSuccess()) {
                System.out.println("Файл успешно передан");
                network.stop();
            }
        });
    }

    private void sendFile(Path path, ChannelFutureListener finishListener) throws IOException {
        Channel channel = network.getCurrentChannel();

        FileRegion region = new DefaultFileRegion(path.toFile(), 0, Files.size(path));

        ByteBuf buf;
        buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(MAGIC_BYTE);
        network.getCurrentChannel().writeAndFlush(buf);

        byte[] filenameBytes = path.getFileName().toString().getBytes(StandardCharsets.UTF_8);
        buf = ByteBufAllocator.DEFAULT.directBuffer(Integer.SIZE / Byte.SIZE);
        buf.writeInt(filenameBytes.length);
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(filenameBytes.length);
        buf.writeBytes(filenameBytes);
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(Long.SIZE / Byte.SIZE);
        buf.writeLong(Files.size(path));
        channel.writeAndFlush(buf);

        ChannelFuture transferOperationFuture = channel.writeAndFlush(region);
        if (finishListener != null) {
            transferOperationFuture.addListener(finishListener);
        }
    }
}