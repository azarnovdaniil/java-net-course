package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IncomingFileHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ура!");
        FileContainer container=(FileContainer) msg;
        Path test = Paths.get("server\\src\\main\\java\\ru\\daniilazarnov\\"+container.getName());

        if (!test.toFile().exists()) {
            Files.createFile(test);
        }
        RandomAccessFile rnd = new RandomAccessFile(test.toString(),"rw");
        rnd.seek(rnd.length());
        rnd.write(container.getFilePart());

    }
}
