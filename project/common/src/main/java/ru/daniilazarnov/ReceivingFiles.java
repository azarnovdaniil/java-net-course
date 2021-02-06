package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReceivingFiles {
    private static State currentState = State.IDLE;
    private static String fileName;
    private static int nextLength;
    private static long fileLength;
    private static long receivedFileLength;
    private static BufferedOutputStream out;



    public static void fileDownload(ChannelHandlerContext ctx, Object msg, ByteBuf buf) throws IOException {
        buf.resetReaderIndex();
        buf = ((ByteBuf) msg);

        if (currentState == State.IDLE) {
            byte readed = buf.readByte();
            if (readed == (byte) 25) {
                currentState = State.NAME_LENGTH;
                System.out.println("(fileDownload) STATE: Start file receiving");
            } else {
                System.out.println("(fileDownload) ERROR: Invalid first byte - " + readed);
            }
        }


        if (currentState == State.IDLE) {
                currentState = State.NAME_LENGTH; //присваиваем текущий статус передачи
                System.out.println("STATE: Start file receiving");
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
}
