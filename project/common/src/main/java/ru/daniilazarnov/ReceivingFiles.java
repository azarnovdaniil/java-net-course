package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import javax.swing.*;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReceivingFiles {
    //    private static State currentState = State.NAME_LENGTH;
    private static State currentState = State.IDLE;
    private static String fileName;
    private static final String DEFAULT_PATH_SERVER = "project/server/cloud_storage/";
    private static final String DEFAULT_PATH_USER = "project/client/local_storage/";
    private static final String username = "user";
    private static final String prefixFileName = "/_";
    private static final String serverName = "server";
    private static int nextLength;
    private static long fileLength;
    private static long receivedFileLength;
    private static BufferedOutputStream out;


    public static void fileReceive(Object msg, String user) throws IOException {
        // собираем путь до целевой папки
        final String FULL_PATH_TO_FILE = user.equals(serverName) ? // собираем путь до целевой папки
                DEFAULT_PATH_SERVER +
                        "user1" +
                        prefixFileName :
                DEFAULT_PATH_USER + prefixFileName
                        .replace("/", "");


        ByteBuf buf = ((ByteBuf) msg);
        if (currentState == State.IDLE) {
            byte readed = buf.readByte();
            if (readed == (byte) 25) {
                currentState = State.NAME_LENGTH;
                System.out.println("Client: Start file receiving");
            } else {
                System.out.println("(class ReceivingFiles)ERROR: Invalid first byte - " + readed);
                buf.resetReaderIndex();
                return;

            }
        }

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
//                out = new BufferedOutputStream(new FileOutputStream("project/server/cloud_storage/user1/_" + new String(fileName)));
                out = new BufferedOutputStream(new FileOutputStream(FULL_PATH_TO_FILE + new String(fileName)));
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
        buf.clear();
    }


}
