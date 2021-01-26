package ru.daniilazarnov;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void oldWay() {
        File file = new File("/unrealpath/file.txt");
        assertFalse(file.exists());
    }

    @Test
    void readFromResources() throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("file.txt");
        Path path = Paths.get(resource.toURI());

        assertTrue(Files.exists(path));
    }

    @Test
    void testPath0() {
        /*get - из переданной строки или URI возвращает объект типа Path*/
        Path path0 = Paths.get("dir/2.txt");
        Path path1 = Paths.get("dir" + File.separator + "2.txt");
        Path path2 = Paths.get("dir", "2.txt");

        assertTrue(Files.exists(path0));
        assertTrue(Files.exists(path1));
        assertTrue(Files.exists(path2));

        Path path = Path.of("/unrealpath/file.txt");
        assertFalse(Files.exists(path));
    }

    @Test
    void testRelativize() {
        /*Path relativize() — вычисляет относительный путь между текущим и переданным путем*/
        Path path0 = Paths.get("dir/2.txt");
        Path path1 = Paths.get("dir/subdir0/file.txt");

        Path relativize = path0.relativize(path1);
        System.out.println(relativize);

        System.out.println(path0);
        System.out.println(path1);
    }

    @Test
    void testResolve() {
        /*Восстанавливает абсолютный путь по текущему и относительному.*/
        Path path0 = Paths.get("dir/subdir");
        Path path1 = Paths.get("subdir2");

        // dir/subdir + subdir2
        System.out.println(path0.resolve("subdir2")); // dir\subdir\subdir2
        System.out.println(path0.resolve(path1));  //dir\subdir\subdir2

        // user -> root_dir 76fewf83fwefwfwef192409fwe18 + my_love.jpg

        // dir/subdir2
        System.out.println(path0.resolveSibling("subdir2")); //dir\subdir2
        System.out.println(path0.resolveSibling(path1)); //dir\subdir2

        System.out.println(path0); //dir\subdir
        System.out.println(path1); //subdir2

        Path path3 = Paths.get("dir/fregergergonsdpkogfnjklfaijpgnljbkflgiofpnljbfk dsgmi");
        System.out.println(path3); //dir\fregergergonsdpkogfnjklfaijpgnljbkflgiofpnljbfk dsgmi
    }

    @Test
    void testStartEndWith() {
        Path path0 = Paths.get("dir/subdir");
        /*startsWith(), endsWith() — проверяют, начинается/заканчивается ли путь с переданного пути*/
        assertTrue(path0.startsWith("dir"));
        assertTrue(path0.endsWith(Paths.get("subdir")));
    }

    @Test
    void testIsAbsolute() {
        Path path0 = Paths.get("dir/subdir");
        /*Приводит путь к абсолютному, если был относительный.*/
        Path absolutePath = path0.toAbsolutePath();
        System.out.println(absolutePath);

        assertTrue(absolutePath.isAbsolute());
    }

    @Test
    void testFileNameParentRoot() {
        Path path0 = Paths.get("/dir/subdir");
        /*Возвращает имя файла из текущего пути*/
        System.out.println(path0.getFileName());
        /*Возвращает директорию из текущего пути.*/
        System.out.println(path0.getParent());
        /*Возвращает корень текущего пути – директорию самого верхнего уровня.*/
        System.out.println(path0.getRoot());
    }

    @Test
    void testCount() {
        Path path0 = Paths.get("/dir/subdir/subsubdir");
        /*getNameCount() используется для возврата количества элементов имени в пути.
        Если этот путь представляет только корневой компонент, метод вернет 1.*/
        System.out.println(path0.getNameCount());
        /*используется для возврата элемента имени этого пути в качестве объекта Path.
        Мы передали индекс в качестве параметра, а индекс представляет индекс элемента name,
        который нужно вернуть. Элемент, ближайший к корню в иерархии каталогов, имеет индекс 0.
        Элемент, самый дальний от корня, имеет индекс count-1.*/
        System.out.println(path0.getName(0));
        System.out.println(path0.getName(path0.getNameCount() - 1));
        System.out.println(path0.subpath(1, path0.getNameCount()));
    }

    @Test
    void testNormalize() {
        Path path0 = Paths.get("/../../../dir/subdir");
        /*используемый для возврата пути из текущего пути,
        в котором исключены все избыточные элементы имени.*/
        System.out.println(path0.normalize());  // \dir\subdir
    }

    @Test
    void testUri() {
        Path path0 = Paths.get("dir/subdir");
        /*Метод toUri() возвращает URI (путь который может быть открыт из браузера)*/
        System.out.println(path0.toUri());
        /*используется для возврата объекта java.io.File, представляющего этот объект пути*/
        System.out.println(path0.toFile());
    }

    @Test
    void nameIterator() {
        Path path0 = Paths.get("dir/subdir");
        /*для возврата итератора элементов имени, которые создают этот путь.*/
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
    void testExists() throws IOException {
        Path path = Paths.get("dir/2.txt");

        assertTrue(Files.exists(path));

        assertTrue(Files.isRegularFile(path));
        assertTrue(Files.isWritable(path));

        assertFalse(Files.isSymbolicLink(path));
        assertTrue(Files.isExecutable(path));
        assertFalse(Files.isHidden(path));
    }

    @Test
    void testIs() throws IOException {
        Path path0 = Paths.get("dir");
        Path path1 = Paths.get("dir");
        assertTrue(Files.isDirectory(path0));
        /*не указывает ли на тот же самый файл?*/
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
