package clientserver.commands;

import clientserver.FileLoaded;
import clientserver.FilePartLoaded;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

public class CommandUpload implements Command {
    //[команда 1б][длина сообщения 4б][номер части 4б] prefix{[хэш 4б][длина имени 4б][имя][размер файла 8б]} [содержимое]
    //если номер части =-1, значит она одна

    public static int getPartSize() {
        return PART_SIZE;
    }

    private static final int PART_SIZE = 10*1024 * 1024;

    @Override
    public void send(ChannelHandlerContext ctx, String content, byte signal) {
        // клиент отправляет на сервер
        byte[] bytes = makePrefix(content); // служебные данные в начале сообщения
        if (bytes.length > 0) {
            try {
                Path file = Path.of(content);

                long volume = Files.size(file);
                long sendVolume = 0;
                int countPar = (int) volume / PART_SIZE + 1;

                int lenServiceData = 1 + 4 + 4 + bytes.length;
                byte[] buffer = new byte[PART_SIZE - lenServiceData];

                int partNum = countPar == 1 ? -1 : 1;
                int i=0;
                FileInputStream fileInputStream = new FileInputStream(file.toFile());
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream, PART_SIZE - lenServiceData);
                while (sendVolume<volume) {
                    int readLength = bufferedInputStream.read(buffer);
                    ByteBuf bufOut = ByteBufAllocator.DEFAULT.buffer(PART_SIZE);
                    bufOut.writeByte(signal); // команда
                    bufOut.writeInt(lenServiceData + readLength); // длина сообщения
                    bufOut.writeInt(partNum + i); // 3 номер части
                    bufOut.writeBytes(bytes); // служебные данные. все, кроме содержимого файла
                    bufOut.writeBytes(buffer, 0, readLength); // содержимое файла

                    ctx.writeAndFlush(bufOut);
                    sendVolume += readLength;
                    i ++;
                    System.out.println("отправлено байт: " + (readLength + lenServiceData));
//                    bufOut.release();
                }
                bufferedInputStream.close();
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Файл не найден");
        }
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, String currentDir, Map<Integer, FileLoaded> uploadedFiles) {
        // сервер принимает от клиента и дает ответ
        buf.readInt(); // длина всего сообщения
        int partNum = buf.readInt();
        int hash = buf.readInt();
        int fileNameSize = buf.readInt();
        ByteBuf fileNameBuf = buf.readBytes(fileNameSize);
        String fileName = fileNameBuf.toString(StandardCharsets.UTF_8);
        fileNameBuf.release();
        long fileSize = buf.readLong();
        FileLoaded fileLoaded;
        Path path = Path.of(currentDir, fileName);
        if (uploadedFiles.containsKey(hash)) {
            fileLoaded = uploadedFiles.get(hash);
        } else {
            fileLoaded = new FileLoaded(hash, fileName, fileSize, path);
            uploadedFiles.put(hash, fileLoaded);
        }

        try {
//            Path path = Path.of(currentDir, fileLoaded.getName());
            int writeSize = buf.readableBytes();
            byte[] bufIn = new byte[writeSize];

//            long startIndex = fileLoaded.getSizeCounter() - writeSize;

            buf.readBytes(bufIn);
            fileLoaded.addPart(partNum, writeSize, bufIn);

            FileOutputStream fileOutputStream = new FileOutputStream(path.toFile(), true);
            fileOutputStream.write(bufIn);
            fileOutputStream.close();

//            RandomAccessFile raFile = new RandomAccessFile(path.toFile(), "rw");
//            raFile.seek(startIndex);
//            raFile.write(bufIn);
//            raFile.close();

            if (fileLoaded.getSizeCounter()==fileSize) {
                System.out.println("Сохранен файл " + fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf) {
        // клиент обрабатывает что получил от сервера

    }

    public static byte[] makePrefix(String fileName) {
        // создание массива со служебными данными
        File file = new File(fileName);
        if (!file.exists()) return new byte[]{(byte) 0};
        String name = file.getName();
        int hash = getHash(file);
        int lengthResponse;
        long volume = file.length();
        lengthResponse = 4 + 4 + name.getBytes(StandardCharsets.UTF_8).length + 8;
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(lengthResponse);

        buf.writeInt(hash); // 4 хэш
        buf.writeInt(name.getBytes(StandardCharsets.UTF_8).length); // 5 длина имени
        buf.writeBytes(name.getBytes(StandardCharsets.UTF_8)); // 6 имя
        buf.writeLong(volume);

        byte[] bufOut = new byte[lengthResponse];
        buf.readBytes(bufOut, 0, lengthResponse);
        return bufOut;
    }


    private static int getHash(File file) {
        return file.hashCode();
    }

}