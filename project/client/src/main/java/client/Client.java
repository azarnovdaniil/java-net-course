package client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

public class Client {

    private final Network network;

    public Client(CountDownLatch networkStarter) {
        this.network = new Network();
        network.start(networkStarter);
    }

    public static void main(String[] args) throws Exception {
        CountDownLatch networkStarter = new CountDownLatch(1);
        Client protoClient = new Client(networkStarter);

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
        buf.writeByte((byte) 25);
        network.getCurrentChannel().writeAndFlush(buf);

        byte[] filenameBytes = path.getFileName().toString().getBytes(StandardCharsets.UTF_8);
        buf = ByteBufAllocator.DEFAULT.directBuffer(4);
        buf.writeInt(filenameBytes.length);
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(filenameBytes.length);
        buf.writeBytes(filenameBytes);
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(8);
        buf.writeLong(Files.size(path));
        channel.writeAndFlush(buf);

        ChannelFuture transferOperationFuture = channel.writeAndFlush(region);
        if (finishListener != null) {
            transferOperationFuture.addListener(finishListener);
        }
    }
}
