package ru.daniilazarnov.common.files;

import io.netty.buffer.ByteBuf;
import ru.daniilazarnov.common.FilePackageConstants;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class FileReader {

    private ReadState state = ReadState.NAME_LENGTH;
    private String fileName;

    private int nextLength;
    private long fileLength;
    private long receivedFileLength;
    private FileChannel fileChannel;
    private String root;

    public FileReader(String root) {
        this.root = root;
    }

    public void readFile(ByteBuf buf) throws IOException {
        if (state == ReadState.NAME_LENGTH) {
            if (buf.readableBytes() >= FilePackageConstants.NAME_LENGTH_BYTES.getCode()) {
                nextLength = buf.readInt();
                state = ReadState.NAME;
            }
        }

        if (state == ReadState.NAME) {
            if (buf.readableBytes() >= nextLength) {
                try {
                    byte[] fileNameBytes = new byte[nextLength];
                    buf.readBytes(fileNameBytes);
                    fileName = new String(fileNameBytes, StandardCharsets.UTF_8);
                    System.out.println("Filename to receive: " + fileName);

                    Path path = Path.of(root + fileName);
                    Files.createDirectories(path.getParent());

                    fileChannel = FileChannel.open(path,
                            EnumSet.of(StandardOpenOption.CREATE,
                                    StandardOpenOption.TRUNCATE_EXISTING,
                                    StandardOpenOption.WRITE));

                    state = ReadState.FILE_LENGTH;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (state == ReadState.FILE_LENGTH) {
            if (buf.readableBytes() >= FilePackageConstants.FILE_LENGTH_BYTES.getCode()) {
                fileLength = buf.readLong();
                System.out.println("File length to receive (bytes): " + fileLength);
                state = ReadState.FILE;
            }
        }

        if (state == ReadState.FILE) {
            try {
                receivedFileLength += buf.readBytes(fileChannel, receivedFileLength, buf.readableBytes());
                if (fileLength == receivedFileLength) {
                    state = ReadState.COMPLETE;
                    System.out.println("File has been received");
                    fileChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ReadState getState() {
        return state;
    }
}
