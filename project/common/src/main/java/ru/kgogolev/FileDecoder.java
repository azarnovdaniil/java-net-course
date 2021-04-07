package ru.kgogolev;

import io.netty.buffer.ByteBuf;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileDecoder {

    public FileDecoder(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    private String workingDirectory;

    private static final byte MAGIC_BYTE = (byte) 25;
    private State currentState = State.IDLE;
    private int nextLength;
    private long fileLength;
    private long receivedFileLength;
    private BufferedOutputStream out;

    public void decodeFile(ByteBuf buf) throws IOException {
        while (buf.readableBytes() > 0) {
            if (currentState == State.IDLE) {
                byte readed = buf.readByte();
                if (readed == MAGIC_BYTE) {
                    currentState = State.NAME_LENGTH;
                    receivedFileLength = 0L;
                    System.out.println("STATE: Start file receiving");
                } else {
                    System.out.println("ERROR: Invalid first byte - " + readed);
                }
            }


            if (currentState == State.NAME_LENGTH) {
                if (buf.readableBytes() >= (Integer.SIZE / Byte.SIZE)) {
                    System.out.println("STATE: Get filename length");
                    nextLength = buf.readInt();
                    currentState = State.NAME;
                }
            }

            if (currentState == State.NAME) {
                if (buf.readableBytes() >= nextLength) {
                    byte[] fileName = new byte[nextLength];
                    buf.readBytes(fileName);
                    File file = new File(workingDirectory, "_" + new String(fileName));
                    file.createNewFile();
                    System.out.println("STATE: Filename received - _" + new String(fileName, "UTF-8"));
                    out = new BufferedOutputStream(new FileOutputStream(file));
                    currentState = State.FILE_LENGTH;
                }
            }

            if (currentState == State.FILE_LENGTH) {
                if (buf.readableBytes() >= (Long.SIZE / Byte.SIZE)) {
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
                        break;
                    }
                }
            }
        }

    }
}
