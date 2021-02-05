package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class FileRecieveHandler extends ChannelInboundHandlerAdapter {
    public enum State {
        IDLE, NAME_LENGTH, NAME, FILE_LENGTH, FILE
    }

    private State currentState = State.IDLE;
    private String fileName;
    private int nextLength;
    private long fileLength;
    private long receivedFileLength;
    private BufferedOutputStream out;

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
                byte[] b = {readed};

//                buf.writeBytes(b);
                buf.resetReaderIndex();
                ctx.fireChannelRead(buf);
                ctx.fireChannelRead(msg);
                return;
            }
        }

        if (currentState == State.NAME_LENGTH) {
            if (buf.readableBytes() >= 4) {
                System.out.println("STATE: Get filename length");
                nextLength = buf.readInt();
                currentState = State.NAME;
            }
        }

        if (currentState == State.NAME) {
            if (buf.readableBytes() >= nextLength) {
                byte[] fileNameByteArr = new byte[nextLength];
                buf.readBytes(fileNameByteArr);
                fileName = new String(fileNameByteArr);

                System.out.println("STATE: Filename received - _" + fileName);
                out = new BufferedOutputStream(new FileOutputStream("project/server/cloud_storage/user1/_" + new String(fileName)));
                currentState = State.FILE_LENGTH;
            }
        }

        if (currentState == State.FILE_LENGTH) {
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
                    ctx.writeAndFlush("[SERVER: File '" + fileName +
                            "' received]");
                    out.close();
                }
            }
        }

        buf.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
