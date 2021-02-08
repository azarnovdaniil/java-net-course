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
    private State currentState = State.IDLE;
    private int nextLength;
    private long fileLength;
    private long receivedFileLength;
    private BufferedOutputStream out;


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
        ByteBuf buf = ((ByteBuf) msg);

        if (currentState == State.IDLE) {
            byte readed = buf.readByte();
            if (readed == (byte) 25) {
                currentState = State.NAME_LENGTH;
                System.out.println("STATE: Start file receiving");
            } else {
                System.out.println("ERROR: Invalid first byte - " + readed);
                buf.resetReaderIndex();
                ctx.fireChannelRead(buf);
//                ctx.fireChannelRead(msg);
                return;
            }
        }

        ReceivingFiles.fileRecieve(buf);
//        fileRecieve(buf);
//        else { // TODO: 08.02.2021 неправильно
//                System.out.println("ERROR: Invalid first byte - " + readed);
//            buf.resetReaderIndex();
//            ctx.fireChannelRead(buf);
//            ctx.fireChannelRead(msg);
//            return;
//        }

    }

    private void fileRecieve(ByteBuf buf) throws IOException {
        if (currentState == State.NAME_LENGTH) {
            if (buf.readableBytes() < 4) return;
            if (buf.readableBytes() >= 4) {
                System.out.println("STATE: Get filename length");
                nextLength = buf.readInt();
                currentState = State.NAME;
            }
        }

        if (currentState == State.NAME) {
            if (buf.readableBytes() < nextLength) return;
            if (buf.readableBytes() >= nextLength) {
                byte[] fileName = new byte[nextLength];
                buf.readBytes(fileName);
                System.out.println("STATE: Filename received - _" + new String(fileName));
                out = new BufferedOutputStream(new FileOutputStream("project/server/cloud_storage/user1/_" + new String(fileName)));
                currentState = State.FILE_LENGTH;
            }
        }

        if (currentState == State.FILE_LENGTH) {
            if (buf.readableBytes() < 8) return;
            if (buf.readableBytes() >= 8) {
                fileLength = buf.readLong();
                System.out.println("STATE: File length received - " + fileLength);
                currentState = State.FILE;
            }
        }

        if (currentState == State.FILE) {
            while (buf.readableBytes() > 0) {
                out.write(buf.readByte());
                receivedFileLength++;
                if (fileLength == receivedFileLength) {
                    currentState = State.IDLE;
                    System.out.println("File received");
                    out.close();
                }
            }
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause);
//        cause.printStackTrace();
        ctx.close();
    }
}
