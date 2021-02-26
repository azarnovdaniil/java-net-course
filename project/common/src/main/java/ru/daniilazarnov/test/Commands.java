package ru.daniilazarnov.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Commands {

    public void listFiles(String directory, ChannelHandlerContext ctx, ChannelFutureListener finishListener) throws IOException {
        File dir = new File(directory);
        File[] files = dir.listFiles();
        ArrayList<String> list = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    list.add(String.format("[%s] size: %s", file.getName(), Utils.bytesConverter(file.length())));
                }
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(list);
        byte[] bytes = bos.toByteArray();
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        buf.writeByte((byte) 45);
        buf.writeBytes(Utils.convertToByteBuf(bytes));
        ChannelFuture transferOperationFuture = ctx.writeAndFlush(buf);
        if (finishListener != null) {
            transferOperationFuture.addListener(finishListener);
        }
    }

    public void removeFile(String path) throws IOException {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch(NoSuchFileException e){
            System.out.println("No such file/directory exists");
        } catch(DirectoryNotEmptyException e){
            System.out.println("Directory is not empty.");
        } catch(IOException e){
            System.out.println("Invalid permissions.");
        }

        System.out.println("Deletion successful.");
    }
}
