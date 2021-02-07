package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    public static void sendFile(Channel channel, ChannelFutureListener finishListener) throws IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
        int send = chooser.showDialog(null, "Send");
        if (send == JFileChooser.APPROVE_OPTION) {
            Path path = Paths.get(chooser.getSelectedFile().getPath());
            FileRegion region = new DefaultFileRegion(path.toFile(), 0, Files.size(path));

            ByteBuf buf = null;
            buf = ByteBufAllocator.DEFAULT.directBuffer(1);
            buf.writeByte((byte) 25);
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
}
