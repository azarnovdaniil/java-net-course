package ru.gb.putilin.cloudstorage.client.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import ru.gb.putilin.cloudstorage.client.FileManager;
import ru.gb.putilin.cloudstorage.common.OperationType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesService implements FileManager {

    private final ClientConnection clientConnection;

    public FilesService(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    @Override
    public void uploadFile(String path) {
        try {
            sendFile(Paths.get(path),
                    future -> {
                        if (!future.isSuccess()) {
                            future.cause().printStackTrace();
                        }
                        if (future.isSuccess()) {
                            System.out.println("Файл успешно передан!");
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void downloadFile(String path) {

    }

    @Override
    public void showFiles() {

    }

    private void sendFile(Path path, ChannelFutureListener finishListener) throws IOException {
        Channel channel = clientConnection.getCurrentChannel();

        FileRegion region = new DefaultFileRegion(path.toFile(), 0, Files.size(path));

        ByteBuf buf;
        buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(OperationType.UPLOAD.getCode());
        channel.writeAndFlush(buf);

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
