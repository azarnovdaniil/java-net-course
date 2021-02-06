package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class FileRecieveHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = Logger.getLogger(FileRecieveHandler.class);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("[SERVER: Пользователь подключился]");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("[SERVER: Пользователь отключился]");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //send file_to_send.txt
        ByteBuf  buf = null;
         buf = ((ByteBuf) msg);
//        if (buf.readableBytes() == 1) {
            byte readed = buf.readByte();

            if (readed == (byte) 25) { //проверяем
                System.out.println("SERVER: file download started...");
                ReceivingFiles.fileDownload(ctx, msg, buf);
            } else {
                System.out.println("ERROR: Invalid first byte - " + readed);
                buf.resetReaderIndex();
                ctx.fireChannelRead(buf);
                ctx.fireChannelRead(msg);
                return;
            }



//        }





    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause);
//        cause.printStackTrace();
        ctx.close();
    }
}
