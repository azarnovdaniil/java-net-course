package clientserver.commands;

import clientserver.BufWorker;
import clientserver.FileLoaded;
import clientserver.FileWorker;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
  [команда 1б][длина сообщения 4б][кол объектов 4 байта][длина имени1 4б][имя1][длина имени2 4б][имя2]...
 */

public class CommandLS implements Command {

    @Override
    public void send(ChannelHandlerContext ctx, String content, FileWorker fileWorker, byte signal) {
        ByteBuf byBuf = ByteBufAllocator.DEFAULT.buffer();
        byBuf.writeByte(signal);
        byBuf.writeInt(5);
        ctx.writeAndFlush(byBuf);
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer, FileLoaded> uploadedFiles, byte signal) {
        try {
            List<String> filesInDir = fileWorker.getFileListInDir();
            fileWorker.sendCommandWithStringList(ctx, filesInDir, signal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer, FileLoaded> uploadedFiles) {
        System.out.println(BufWorker.readFileListFromBuf(buf));
    }

}
