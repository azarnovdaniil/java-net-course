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
    void testWalk() throws IOException {
        Path path = Paths.get("files/catalog/ddd/sd.jpg");
        System.out.println(path.getParent());

//        //Stream<Path> stream = Files.walk(Path.of("files/catalog/lol/hh.jpg"));
//        Files.createDirectory(Path.of("files/catalog/lol"));
//        //stream.forEach(System.out::println);
    }

    @Test
    void testFileVisitResult() {
        for (FileVisitResult value : FileVisitResult.values()) {
            System.out.println(value);
        }
    }

    @Test
    void testWalkFileTree() throws IOException {
        Files.walkFileTree(Path.of("files/catalog/lol/ll.e"), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println(dir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                System.out.println(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                System.out.println(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Test
    void testDeleteEmptyDirectory() throws IOException {
        Files.createDirectory(Path.of("empty_dir"));

        assertTrue(Files.exists(Path.of("empty_dir")));

        Files.delete(Path.of("empty_dir"));

        assertFalse(Files.exists(Path.of("empty_dir")));
    }

    @Test
    void testDeleteNotEmptyDirectory() throws IOException {
        Files.createDirectory(Path.of("dir_with_file"));
        Files.createFile(Path.of("dir_with_file/file.txt"));

        assertTrue(Files.exists(Path.of("dir_with_file")));
        assertThrows(DirectoryNotEmptyException.class, () -> Files.delete(Path.of("dir_with_file")));

        assertTrue(Files.exists(Path.of("dir_with_file")));
        assertTrue(Files.exists(Path.of("dir_with_file/file.txt")));

        Files.walkFileTree(Path.of("dir_with_file"), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });

        assertFalse(Files.exists(Path.of("dir_with_file")));
        assertFalse(Files.exists(Path.of("dir_with_file/file.txt")));
    }

    @Test
    void testFileChannel() throws IOException {
        Path srcPath = Path.of("channel_example_1.txt");

        RandomAccessFile src = new RandomAccessFile(srcPath.toFile(), "rw");
        FileChannel srcChannel = src.getChannel();

        System.out.println(Files.readString(srcPath));


        RandomAccessFile dest = new RandomAccessFile(Path.of("channel_example_2.txt").toFile(), "rw");
        FileChannel destChannel = dest.getChannel();

        srcChannel.transferTo(0, srcChannel.size(), destChannel);

        //DatagramChannel - UDP
        //SocketChannel - TCP
        //ServerSocketChannel - listen all TCP-connections
    }

    @Test
    void testBuffer0() throws IOException {
        byte arr[] = {10, 20, 30, 40, 50};
        ByteBuffer buffer = ByteBuffer.wrap(arr);

        System.out.println("capacity: " + buffer.capacity());
        System.out.println("limit: " + buffer.limit());
        System.out.println("position: " + buffer.position());
        System.out.println(buffer);
    }

    @Test
    void testBuffer1() throws IOException {
        byte arr[] = {10, 20, 30, 40, 50};
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
    void testBuffer2() throws IOException {
        byte arr[] = {10, 20, 30, 40, 50};
        byte arr1[] = new byte[10];


        ByteBuffer buffer = ByteBuffer.wrap(arr);

        System.out.println("get: " + buffer.get());
        System.out.println("get: " + buffer.get());
        System.out.println();

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
    void testBuffer3() throws IOException {
        byte arr[] = {10, 20, 30, 40, 50};
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
    void testBuffer4() throws IOException {
        byte arr[] = {10, 20, 30, 40, 50};
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
    void testBuffer5() throws IOException {
        byte arr[] = {10, 20, 30, 40, 50};
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

    @Test
    void testWrite() throws IOException {
        Path destPath = Path.of("channel_example_3.txt");

        RandomAccessFile dest = new RandomAccessFile(destPath.toFile(), "rw");
        FileChannel destChannel = dest.getChannel();

        String newData = "New String to write to file..." + System.currentTimeMillis();

        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put(newData.getBytes());

        buf.flip();

        while (buf.hasRemaining()) {
            destChannel.write(buf);
        }
        destChannel.close();
    }

    @Test
    void testTruncate() throws IOException {
        Path path = Path.of("channel_example_4.txt");

        RandomAccessFile file = new RandomAccessFile(path.toFile(), "rw");
        FileChannel channel = file.getChannel();

        System.out.println(channel.position());
        System.out.println();
        System.out.println(channel.size());

        channel.truncate(channel.size() - 20);
        System.out.println(channel.size());
        channel.close();
    }
}
