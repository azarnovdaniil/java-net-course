package ru.daniilazarnov;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FilesExamplesTest {

    @Test
    void testCopy() throws IOException {
        Files.copy(Path.of("dir/from.txt"), Path.of("dir/to.txt"), StandardCopyOption.REPLACE_EXISTING);

        OutputStream outputStream = new FileOutputStream("dir/out.txt");
        Files.copy(Path.of("dir/from.txt"), outputStream);

        InputStream inputStream = new FileInputStream("dir/from.txt");
        Files.copy(inputStream, Path.of("dir/from_input.txt"), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void testWalk() throws IOException {
        Stream<Path> stream = Files.walk(Path.of("../lesson-1/dir"));

        stream.forEach(System.out::println);
    }

    @Test
    void testFileVisitResult() {
        for (FileVisitResult value : FileVisitResult.values()) {
            System.out.println(value);
        }
    }

    @Test
    void testWalkFileTree() throws IOException {
        Files.walkFileTree(Path.of("../lesson-1/dir"), new FileVisitor<Path>() {
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
                Files.delete(file);
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
        Path srcPath = Path.of("dir/channel_example_1.txt");

        RandomAccessFile src = new RandomAccessFile(srcPath.toFile(), "rw");
        FileChannel srcChannel = src.getChannel();

        System.out.println(Files.readString(srcPath));

        RandomAccessFile dest = new RandomAccessFile(Path.of("dir/channel_example_2.txt").toFile(), "rw");
        FileChannel destChannel = dest.getChannel();

        srcChannel.transferTo(0, srcChannel.size(), destChannel);

        //DatagramChannel - UDP
        //SocketChannel - TCP
        //ServerSocketChannel - listen all TCP-connections
    }

    @Test
    void testTruncate() throws IOException {
        Path path = Path.of("dir/channel_example_4.txt");

        RandomAccessFile file = new RandomAccessFile(path.toFile(), "rw");
        FileChannel channel = file.getChannel();

        System.out.println(channel.position());
        System.out.println();
        System.out.println(channel.size());

        channel.truncate(channel.size() - 1);
        System.out.println(channel.size());
        channel.close();
    }
}
