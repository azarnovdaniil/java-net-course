package ru.daniilazarnov;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testPath0() {
        Path path0 = Paths.get("dir/2.txt");
        Path path1 = Paths.get("dir" + File.separator + "2.txt");
        Path path2 = Paths.get("dir", "2.txt");

        assertTrue(Files.exists(path0));
        assertTrue(Files.exists(path1));
        assertTrue(Files.exists(path2));
    }

    @Test
    void testRelativize() {
        Path path0 = Paths.get("dir/2.txt");
        Path path1 = Paths.get("dir/subdir0/file.txt");

        System.out.println(path0.relativize(path1));

        System.out.println(path0);
        System.out.println(path1);
    }

    @Test
    void testResolve() {
        Path path0 = Paths.get("dir/subdir");
        Path path1 = Paths.get("subdir2");

        // dir/subdir + subdir2
        System.out.println(path0.resolve("subdir2"));
        System.out.println(path0.resolve(path1));

        // user -> root_dir 76fewf83fwefwfwef192409fwe18 + my_love.jpg

        // dir/subdir2
        System.out.println(path0.resolveSibling("subdir2"));
        System.out.println(path0.resolveSibling(path1));

        System.out.println(path0);
        System.out.println(path1);

        Path path3 = Paths.get("dir/fregergergonsdpkogfnjklfaijpgnljbkflgiofpnljbfk dsgmi");
        System.out.println(path3);
    }

    @Test
    void testStartEndWith() {
        Path path0 = Paths.get("dir/subdir");

        assertTrue(path0.startsWith("dir"));
        assertTrue(path0.endsWith(Paths.get("subdir")));
    }

    @Test
    void testIsAbsolute() {
        Path path0 = Paths.get("dir/subdir");

        Path absolutePath = path0.toAbsolutePath();
        System.out.println(absolutePath);

        assertTrue(absolutePath.isAbsolute());
    }

    @Test
    void testFileNameParentRoot() {
        Path path0 = Paths.get("/dir/subdir");

        System.out.println(path0.getFileName());
        System.out.println(path0.getParent());
        System.out.println(path0.getRoot());
    }

    @Test
    void testCount() {
        Path path0 = Paths.get("/dir/subdir/subsubdir");

        System.out.println(path0.getNameCount());
        System.out.println(path0.getName(0));
        System.out.println(path0.getName(path0.getNameCount() - 1));
        System.out.println(path0.subpath(1, path0.getNameCount()));
    }

    @Test
    void testNormalize() {
        Path path0 = Paths.get("/../../../dir/subdir");

        System.out.println(path0.normalize());
    }

    @Test
    void testUri() {
        Path path0 = Paths.get("dir/subdir");

        System.out.println(path0.toUri());
        System.out.println(path0.toFile());
    }

    @Test
    void nameIterator() {
        Path path0 = Paths.get("dir/subdir");

        path0.iterator().forEachRemaining(System.out::println);
    }

    @Test
    void testFileSystem() {
        Path path = Paths.get("dir/2.txt");

        FileSystem fileSystem = path.getFileSystem();

        System.out.println(fileSystem.supportedFileAttributeViews());
        System.out.println(fileSystem);
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testExists() throws IOException {
        Path path = Paths.get("dir/2.txt");

        assertTrue(Files.exists(path));

        assertTrue(Files.isRegularFile(path));
        assertTrue(Files.isWritable(path));

        assertFalse(Files.isSymbolicLink(path));
        assertFalse(Files.isExecutable(path));
        assertFalse(Files.isHidden(path));
    }

    @Test
    void testIs() throws IOException {
        Path path0 = Paths.get("dir");
        Path path1 = Paths.get("dir");
        assertTrue(Files.isDirectory(path0));
        assertTrue(Files.isSameFile(path0, path1));
    }

    @Test
    void testWrite() throws IOException {
        Path path = Paths.get("dir/2.txt");

        Files.write(path, List.of("hello", "world"), StandardOpenOption.WRITE);
        Files.writeString(path, "310287310", StandardOpenOption.APPEND);
    }

    @Test
    void testRead() throws IOException {
        Path path = Paths.get("dir/2.txt");

        System.out.println(Arrays.toString(Files.readAllBytes(path)));
        System.out.println(Files.readAllLines(path));
        System.out.println(Files.readString(path));
    }

    @Test
    void testCopy() throws IOException {
        Files.copy(Path.of("dir/from.txt"), Path.of("dir/to.txt"), StandardCopyOption.REPLACE_EXISTING);

        OutputStream outputStream = new FileOutputStream("dir/out.txt");
        Files.copy(Path.of("dir/from.txt"), outputStream);

        InputStream inputStream = new FileInputStream("dir/from.txt");
        Files.copy(inputStream, Path.of("dir/from_input.txt"), StandardCopyOption.REPLACE_EXISTING);
    }
}
