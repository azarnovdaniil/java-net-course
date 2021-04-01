package helpers;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileHelper {

    private static final String OUTPUT_DIRECTORY = "upload";
    private static final String USERNAME_WARNING = "имя пользователя должно быть задано";
    private static final String PATH_WARNING = "путь должен быть задан";
    private static final int BUFFER_CAPACITY = 48;

    //stream version
    public static void saveFile(InputStream from, String username, String filename) {

        if (username.isEmpty()) {
            throw new IllegalArgumentException(USERNAME_WARNING);
        }

        if (filename.isEmpty()) {
            throw new IllegalArgumentException(PATH_WARNING);
        }

        Path out = Path.of("..", OUTPUT_DIRECTORY, username, filename);

        try (OutputStream to = Files.newOutputStream(out)) {
            from.transferTo(to);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //byte version
    public static void getFile(OutputStream to, String username, String filename) throws IOException {
        if (username.isEmpty()) {
            throw new IllegalArgumentException(USERNAME_WARNING);
        }

        if (filename.isEmpty()) {
            throw new IllegalArgumentException(PATH_WARNING);
        }

        ByteBuffer buf = ByteBuffer.allocate(BUFFER_CAPACITY);
        Path p = Path.of("..", OUTPUT_DIRECTORY, username, filename);

        if (p.toFile().exists()) {
            try (RandomAccessFile raf = new RandomAccessFile(p.toFile(), "rw")) {
                FileChannel srcChannel = raf.getChannel();
                int bytesRead = srcChannel.read(buf);

                while (bytesRead != -1) {
                    buf.flip();

                    while (buf.hasRemaining()) {
                        to.write(buf.get());
                    }

                    buf.clear();
                    bytesRead = srcChannel.read(buf);
                }

                srcChannel.close();
            }
        }
    }

    public static void deleteFile(String username, String filename) {
        //todo
    }

    public static void emptyUserFolder(String username) {
        //todo
    }

    public static void copyFile(String fromUser, String toUser) {
        //todo
    }

    public static void renameFile(String username, String oldName, String newName) {
        //todo
    }

    public static List<Path> listDirectories(String username) throws IOException {
        if (username.isEmpty()) {
            throw new IllegalArgumentException(USERNAME_WARNING);
        }

        ArrayList<Path> result = new ArrayList<>();

        Path p = Path.of("..", OUTPUT_DIRECTORY, username);

        if (Files.exists(p)) {
            try (Stream<Path> stream = Files.walk(p)) {
                result = (ArrayList<Path>) stream.filter(x -> x.toFile().isDirectory()).collect(Collectors.toList());
            }
        }

        return result;
    }

    public static List<Path> listFilesInDirectory(String username, String name) throws IOException {
        if (username.isEmpty()) {
            throw new IllegalArgumentException(USERNAME_WARNING);
        }

        if (name.isEmpty()) {
            throw new IllegalArgumentException(PATH_WARNING);
        }

        ArrayList<Path> result = new ArrayList<>();

        Path p = Path.of("..", OUTPUT_DIRECTORY, username, name);

        if (Files.exists(p)) {
            try (Stream<Path> stream = Files.walk(p)) {
                result = (ArrayList<Path>) stream.filter(x -> x.toFile().isFile()).collect(Collectors.toList());
            }
        }

        return result;
    }

    public static boolean createDirectory(String username, String name) throws IOException {
        if (username.isEmpty()) {
            throw new IllegalArgumentException(USERNAME_WARNING);
        }

        if (name.isEmpty()) {
            throw new IllegalArgumentException(PATH_WARNING);
        }

        boolean result = false;

        Path p = Path.of("..", OUTPUT_DIRECTORY, username, name);

        if (!Files.exists(p)) {
            Files.createDirectory(p);
            result = true;
        }
        return result;
    }
}
