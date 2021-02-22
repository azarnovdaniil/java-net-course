package ru.daniilazarnov;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testBuffer0() {
        byte[] arr = {10, 20, 30, 40, 50};
        ByteBuffer buffer = ByteBuffer.wrap(arr);

        System.out.println("capacity: " + buffer.capacity());
        System.out.println("limit: " + buffer.limit());
        System.out.println("position: " + buffer.position());
        System.out.println(buffer);
    }

    @Test
    void testBuffer1() {
        byte[] arr = {10, 20, 30, 40, 50};
        ByteBuffer buffer = ByteBuffer.wrap(arr);

        System.out.println();
        System.out.println("get: " + buffer.get());
        System.out.println("capacity: " + buffer.capacity());
        System.out.println("limit: " + buffer.limit());
        System.out.println("position: " + buffer.position());
        System.out.println(buffer);

        System.out.println();
        System.out.println("get: " + buffer.get());
        System.out.println("capacity: " + buffer.capacity());
        System.out.println("limit: " + buffer.limit());
        System.out.println("position: " + buffer.position());
        System.out.println(buffer);
    }

    @Test
    void testBuffer2() {
        byte[] arr = {10, 20, 30, 40, 50};
        ByteBuffer buffer = ByteBuffer.wrap(arr);

        System.out.println("get: " + buffer.get());
        System.out.println("get: " + buffer.get());
        System.out.println();

        buffer.rewind();
        System.out.println("capacity: " + buffer.capacity());
        System.out.println("limit: " + buffer.limit());
        System.out.println("position: " + buffer.position());
        System.out.println(buffer);
    }

    @Test
    void testBuffer3() {
        byte[] arr = {10, 20, 30, 40, 50};
        ByteBuffer buffer = ByteBuffer.wrap(arr);

        System.out.println("get: " + buffer.get());
        System.out.println("position: " + buffer.position());
        buffer.mark();
        System.out.println("get: " + buffer.get());
        System.out.println("position: " + buffer.position());
        buffer.reset();
        System.out.println("position: " + buffer.position());

        buffer.clear();
        System.out.println("capacity: " + buffer.capacity());
        System.out.println("limit: " + buffer.limit());
        System.out.println("position: " + buffer.position());
    }

    @Test
    void testBuffer4() {
        byte[] arr = {10, 20, 30, 40, 50};
        ByteBuffer buffer = ByteBuffer.wrap(arr);

        System.out.println(buffer.get());
        System.out.println(buffer.get());
        System.out.println();
        buffer.compact();
        System.out.println("capacity: " + buffer.capacity());
        System.out.println("limit: " + buffer.limit());
        System.out.println("position: " + buffer.position());
        System.out.println(buffer);
    }

    @Test
    void testBuffer5() {
        byte[] arr = {10, 20, 30, 40, 50};
        ByteBuffer buffer = ByteBuffer.wrap(arr);

        System.out.println(buffer.get());
        System.out.println(buffer.get());
        System.out.println("capacity: " + buffer.capacity());
        System.out.println("limit: " + buffer.limit());
        System.out.println("position: " + buffer.position());
        System.out.println();
        buffer.flip();
        System.out.println("capacity: " + buffer.capacity());
        System.out.println("limit: " + buffer.limit());
        System.out.println("position: " + buffer.position());
        System.out.println(buffer);
    }

    @Test
    void testReadWithBuffer() throws IOException {
        Path srcPath = Path.of("src/main/java/ru/daniilazarnov/Main.java");

        RandomAccessFile src = new RandomAccessFile(srcPath.toFile(), "rw");
        FileChannel srcChannel = src.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);
        int bytesRead = srcChannel.read(buf);
        while (bytesRead != -1) {
            buf.flip();

            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }
            buf.clear();
            bytesRead = srcChannel.read(buf);
        }
        srcChannel.close();
    }

}
