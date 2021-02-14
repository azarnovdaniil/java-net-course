package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReceivingFiles {
    private static State currentState = State.IDLE;
    private static final String DEFAULT_PATH_SERVER = "project/server/cloud_storage/";
    private static final String DEFAULT_PATH_USER = "project/client/local_storage/";
    private static final String PREFIX_FILE_NAME = "/_";
    private static final String SERVER_NAME = "server";
    private static int nextLength;
    private static long fileLength;
    private static long receivedFileLength;
    private static BufferedOutputStream out;

    public static State getCurrentState() {
        return currentState;
    }

    public static void fileReceive(Object msg, String user) throws IOException {
        String fileNameStr;
        String fullPathString = user.equals(SERVER_NAME) ?
                DEFAULT_PATH_SERVER +
                        "user1" +
                        PREFIX_FILE_NAME :
                DEFAULT_PATH_USER + PREFIX_FILE_NAME
                        .replace("/", "");

        ByteBuf buf = ((ByteBuf) msg);
        if (currentState == State.IDLE) {
            receivedFileLength = 0;
            buf.resetReaderIndex();
            byte readed = buf.readByte();
            if (readed == (byte) 2) {
                currentState = State.NAME_LENGTH;
                System.out.println("currentState changed: " + currentState);
                System.out.println("Client: Start file receiving");
            } else {
                System.out.println("(class ReceivingFiles)ERROR: Invalid first byte - " + readed);
                return;
            }
        }
        if (currentState == State.NAME_LENGTH) {
            if (buf.readableBytes() < 4) return;
            if (buf.readableBytes() >= 4) {
                System.out.println("STATE: Get filename length");
                nextLength = buf.readInt();
                currentState = State.NAME;
                System.out.println("currentState changed: " + currentState);
            }
        }
        if (currentState == State.NAME) {
            if (buf.readableBytes() < nextLength) return;
            if (buf.readableBytes() >= nextLength) {
                byte[] fileName = new byte[nextLength];
                buf.readBytes(fileName);
                fileNameStr = new String(fileName);
                System.out.println("STATE: Filename received - _" + new String(fileName));
                out = new BufferedOutputStream(new FileOutputStream(fullPathString + fileNameStr));
                currentState = State.FILE_LENGTH;
                System.out.println("currentState changed: " + currentState);
            }
        }
        if (currentState == State.FILE_LENGTH) {
            if (buf.readableBytes() < 8) return;
            if (buf.readableBytes() >= 8) {
                fileLength = buf.readLong();
                System.out.println("STATE: File length received - " + fileLength);
                currentState = State.FILE;
                System.out.println("currentState changed: " + currentState);
            }
        }
        if (currentState == State.FILE) {
            while (buf.readableBytes() > 0) {
                out.write(buf.readByte());
                receivedFileLength++;
                if (fileLength == receivedFileLength) {
                    currentState = State.IDLE;
                    System.out.println("currentState changed: " + currentState);
                    System.out.println("File received");
                    if (buf.readableBytes() == 0) {
                        buf.clear();
                    }
                    out.close();
                }
            }
        }

    }
}
