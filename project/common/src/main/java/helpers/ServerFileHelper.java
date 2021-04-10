package helpers;

import io.netty.channel.Channel;
import messages.FileMessage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerFileHelper {

    private static final String OUTPUT_DIRECTORY = "upload";
    private static final String USERNAME_WARNING = "имя пользователя должно быть задано";
    private static final String PATH_WARNING = "путь должен быть задан";
    private static final int BUFFER_CAPACITY = 48;

    public static void saveFile(Object msg, String username, String filename) {

        if (username.isEmpty()) {
            throw new IllegalArgumentException(USERNAME_WARNING);
        }

        if (filename.isEmpty()) {
            throw new IllegalArgumentException(PATH_WARNING);
        }

        ByteBuffer buf = ByteBuffer.allocate(BUFFER_CAPACITY);
        Path p = Path.of("..", OUTPUT_DIRECTORY, username, filename);

        if (!p.toFile().exists()) {
            try {
                Files.write(p, ((FileMessage) msg).getContent(), StandardOpenOption.CREATE_NEW);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendFile(Channel to, String username, String filename) throws IOException {
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

    public static void deleteFile(String username, String filename) throws IOException {
        if (username.isEmpty()) {
            throw new IllegalArgumentException(USERNAME_WARNING);
        }

        if (filename.isEmpty()) {
            throw new IllegalArgumentException(PATH_WARNING);
        }

        if (Files.exists(Path.of("..", OUTPUT_DIRECTORY, username, filename))) {
            Files.delete(Path.of("..", OUTPUT_DIRECTORY, username, filename));
        }
    }

    public static void emptyUserFolder(String username) throws IOException {

        if (username.isEmpty()) {
            throw new IllegalArgumentException(USERNAME_WARNING);
        }

        Files.walkFileTree(Path.of("..", OUTPUT_DIRECTORY, username), new FileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void copyFile(String fromUser, String toUser) throws IOException {

        if (fromUser.isEmpty()) {
            throw new IllegalArgumentException(USERNAME_WARNING);
        }

        if (toUser.isEmpty()) {
            throw new IllegalArgumentException(PATH_WARNING);
        }

        if (Files.exists(Path.of("..", OUTPUT_DIRECTORY, fromUser))) {
            Files.copy(Path.of("..", OUTPUT_DIRECTORY, fromUser),
                    Path.of("..", OUTPUT_DIRECTORY, toUser),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void renameFile(String username, String oldName, String newName) {
        if (username.isEmpty()) {
            throw new IllegalArgumentException(USERNAME_WARNING);
        }

        if (oldName.isEmpty()) {
            throw new IllegalArgumentException(PATH_WARNING);
        }

        if (newName.isEmpty()) {
            throw new IllegalArgumentException(PATH_WARNING);
        }

        if (Files.exists(Path.of("..", OUTPUT_DIRECTORY, username, oldName))) {
            File file = Path.of("..", OUTPUT_DIRECTORY, username, oldName).toFile();
            file.renameTo(Path.of("..", OUTPUT_DIRECTORY, username, newName).toFile());
        }
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
