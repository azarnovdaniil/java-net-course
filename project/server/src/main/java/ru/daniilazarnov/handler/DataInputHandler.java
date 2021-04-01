package ru.daniilazarnov.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.sf.saxon.type.StringConverter;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

public class DataInputHandler extends ChannelInboundHandlerAdapter {

    private final String address;

    DataInputHandler(String address)throws Exception { this.address = address;}

    private static final byte MAGIC_BYTE = 21;
    private State currentState = State.IDLE;
    private int nextLength;
    private long fileLength;
    private long receivedFileLength;
    private BufferedOutputStream out;
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Start upload...");
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (Files.notExists(Path.of(address))){
            Files.createFile(Path.of(address));
        }else {
            Files.delete(Path.of(address));
            Files.createFile(Path.of(address));
        }



        ByteBuf buf = ((ByteBuf) msg);
        while (buf.readableBytes() > 0) {

            if (currentState == State.FILE_LENGTH) {
                if (buf.readableBytes() >= (Long.SIZE / Byte.SIZE)) {
                    fileLength = buf.readLong();
                    System.out.println("STATE: File length received - " + fileLength);
                    currentState = State.FILE;
                }
            }
            out = new BufferedOutputStream(new FileOutputStream(address));

            if (currentState == State.FILE) {
                while (buf.readableBytes() > 0) {
                    out.write(buf.readByte());
                    receivedFileLength++;
                    if (fileLength == receivedFileLength) {
                        currentState = State.IDLE;
                        System.out.println("File received");
                        out.close();
                        break;
                    }
                }
            }
        }
        if (buf.readableBytes() == 0) {
            buf.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}