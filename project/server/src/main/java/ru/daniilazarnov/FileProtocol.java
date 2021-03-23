package ru.daniilazarnov;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileProtocol {
    private static final int BYTE_ARRAY_SIZE = 1024;
    private static final ByteBuffer serviceBuffer = ByteBuffer.allocate(1024);

    public static void receiver(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        byte command = 0;
        //Не пойму, что будет, если гипотетически на момент чтения из канала придут не все байты, которые дальше
        //будут читаться из буфера?
        socketChannel.read(serviceBuffer);
        serviceBuffer.flip();
        int cmd = serviceBuffer.get();
        switch (cmd) {
            //передача файла на сервер
            case 1:
                socketChannel.read(serviceBuffer);
                serviceBuffer.flip();
//                  Размер пересылаемого файла
                int sizeFile = serviceBuffer.getInt();
                ByteBuffer byteBuffer = ByteBuffer.allocate(sizeFile);

//                   Размер имени пересылаемого файла
                int fileNameSize = serviceBuffer.getInt();

//                   Собираем имя файла из буфера
                StringBuilder sb = new StringBuilder();
                while (fileNameSize != 0) {
                    sb.append((char) serviceBuffer.get());
                    fileNameSize--;
                }
                String fileName = sb.toString();

//                   Собираем файл из буфера
                while (sizeFile > 0) {
                    while (serviceBuffer.hasRemaining() && byteBuffer.limit() != byteBuffer.position()) {
                        byteBuffer.put(serviceBuffer.get());
                        sizeFile--;
                    }
                    serviceBuffer.clear();
                    socketChannel.read(serviceBuffer);
                    serviceBuffer.flip();
                }
                Path pathFile = Paths.get(((Path) key.attachment()).toString() + File.separator + fileName);
                Files.write(pathFile, byteBuffer.array());
                break;
            //узнать текущую директорию
            case 2:
                Path path = (Path) key.attachment();
                socketChannel.write(ByteBuffer.wrap(path.toString().getBytes(StandardCharsets.UTF_8)));
                break;

            case 3:


        }
    }

    public static void sender() {

    }
}
