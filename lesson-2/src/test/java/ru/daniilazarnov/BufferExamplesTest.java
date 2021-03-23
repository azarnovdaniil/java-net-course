package ru.daniilazarnov;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BufferExamplesTest {

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
        System.out.println("position: " + buffer.position());
        System.out.println();

        buffer.rewind();
        System.out.println("capacity: " + buffer.capacity());
        System.out.println("limit: " + buffer.limit());
        System.out.println("position: " + buffer.position());
        System.out.println(buffer);
        System.out.println("get: " + buffer.get());
        System.out.println("get: " + buffer.get());
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
        System.out.println("position: " + buf.position());
        int bytesRead = srcChannel.read(buf);
        System.out.println("position: " + buf.position());
        System.out.println("capacity: " + buf.capacity());
        System.out.println("limit: " + buf.limit());
        while (bytesRead != -1) {
            buf.flip();

            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }
            buf.clear();
            bytesRead = srcChannel.read(buf);
            System.out.println("position: " + buf.position());
        }
        srcChannel.close();
    }

    @Test
    void testWrite() {
        String newData = "New String to write to file..." + System.currentTimeMillis();

        ByteBuffer buf = ByteBuffer.allocate(12);
        int iter = 0;

        byte[] bytes = newData.getBytes();
        for (int i = 0; i < bytes.length;) {
            if (buf.position() < buf.capacity()) {
                buf.put(bytes[i]);
                i++;
            } else {
                System.out.println("Before flip: position: " + buf.position() + " limit: " + buf.limit());
                buf.flip();
                System.out.println("After flip: position: " + buf.position() + " limit: " + buf.limit());

                System.out.println("Iteration:" + iter);
                iter++;
                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }
                System.out.println();
                System.out.println("After read: position: " + buf.position() + " limit: " + buf.limit());
                buf.clear();
                System.out.println("After clear: position: " + buf.position() + " limit: " + buf.limit());
                System.out.println();
            }
        }
        System.out.println("Before flip: position: " + buf.position() + " limit: " + buf.limit());
        buf.flip();
        //[12][12][312][42][34][34][55][44]
        System.out.println("After flip: position: " + buf.position() + " limit: " + buf.limit());

        System.out.println("Iteration:" + iter);
        while (buf.hasRemaining()) {
            System.out.print((char) buf.get());
        }
        System.out.println();
        System.out.println("After read: position: " + buf.position() + " limit: " + buf.limit());
        buf.clear();
        System.out.println("After clear: position: " + buf.position() + " limit: " + buf.limit());
        System.out.println();
    }

    @Test
    void testBuffer7() {

        //int - 32bit
        //byte - 8bit
        //long - 64bit

        byte[] arr = {10, 20, 30, 40, 50};
        ByteBuffer buffer = ByteBuffer.wrap(arr);

        System.out.println(buffer.getInt());
        System.out.println("capacity: " + buffer.capacity());
        System.out.println("limit: " + buffer.limit());
        System.out.println("position: " + buffer.position());
    }

    @Test
    void testBuffer8() {
        byte[] arr = {10, 20, 30, 40, 50};
        ByteBuffer buffer = ByteBuffer.wrap(arr);

        assertThrows(BufferUnderflowException.class, buffer::getLong);
    }

    @Test
    void testBufferProtocol() {

        // message length in bytes - int ; information - ???
        byte[] arr = {0, 0, 0, 10, 65, 65, 65, 65, 65, 65, 65, 65, 65, 20, 30, 40, 50, 10, 10, 20, 30, 40, 50, 10};
        //System.out.println(arr.length);
        ByteBuffer buffer = ByteBuffer.wrap(arr);

        System.out.println();

        int messageLength = buffer.getInt();

        for (int i = 0; i < messageLength; i++) {
            System.out.print((char) buffer.get());
        }

        System.out.println();

        System.out.println("capacity: " + buffer.capacity());
        System.out.println("limit: " + buffer.limit());
        System.out.println("position: " + buffer.position());
    }

}
