package common.service;

import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.RandomAccessFile;


public class FileLoadService {
    private final int sizePart = 1024 * 10;
    private int arrayLength;


    public void writeFile(FileLoad fileLoad, ChannelHandlerContext ctx) throws Exception {

        byte[] bytes = fileLoad.getBytes();
        int byteRead = fileLoad.getByteRead();
        int startPos = fileLoad.getStartPos();

        File file = new File(fileLoad.getDstPath());
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

        randomAccessFile.seek(startPos);
        randomAccessFile.write(bytes);
        startPos = startPos + byteRead;

        if (fileLoad.isNotLastPart()) {
            fileLoad.setStartPos(startPos);
            ctx.writeAndFlush(fileLoad);
        }
        randomAccessFile.close();
    }

    public void readFile(FileLoad fileLoad, ChannelHandlerContext ctx) throws Exception {

        int startPos = fileLoad.getStartPos();

        RandomAccessFile randomAccessFile = new RandomAccessFile(fileLoad.getSourcePath(), "r");
        randomAccessFile.seek(startPos);

        long lastPart = (randomAccessFile.length() - startPos);

        if (lastPart > sizePart) {
            arrayLength = sizePart;
        }
        else {
            arrayLength = (int) lastPart;
            fileLoad.setNotLastPart(false);
        }

        byte[] arrayBytes = new byte[arrayLength];
        int byteRead = randomAccessFile.read(arrayBytes);

        arrayLength = sizePart;
        fileLoad.setByteRead(byteRead);
        fileLoad.setBytes(arrayBytes);

        fileLoad.setCountParts((int) (randomAccessFile.length() / sizePart));
        fileLoad.setCountProgress(fileLoad.getCountProgress() + 1);


        ctx.writeAndFlush(fileLoad);
        randomAccessFile.close();
    }
}
