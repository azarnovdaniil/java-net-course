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

    public static void saveFile(InputStream from, Path path) {
        Path out = Path.of(OUTPUT_DIRECTORY + File.separator + path.getFileName());
        try (OutputStream to = Files.newOutputStream(out)) {
            from.transferTo(to);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //todo доделать
    public static byte[] getFile(String username, String filename) throws IOException {
        if(username.isEmpty()) throw new IllegalArgumentException(USERNAME_WARNING);
        if(filename.isEmpty()) throw new IllegalArgumentException(PATH_WARNING);

        byte[] buffer = new byte[0];
        ByteBuffer buf = ByteBuffer.allocate(48);

        Path p = Path.of("..", OUTPUT_DIRECTORY,username, filename);
        try (RandomAccessFile src = new RandomAccessFile(p.toFile(), "rw")) {
            FileChannel srcChannel = src.getChannel();
        }

        return buffer;
    }

    public static List<Path> listDirectories(String username) throws IOException {
        if(username.isEmpty()) throw new IllegalArgumentException(USERNAME_WARNING);

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
        if(username.isEmpty()) throw new IllegalArgumentException(USERNAME_WARNING);
        if(name.isEmpty()) throw new IllegalArgumentException(PATH_WARNING);

        ArrayList<Path> result = new ArrayList<>();

        Path p = Path.of("..", OUTPUT_DIRECTORY,username, name);

        if (Files.exists(p)) {
            try (Stream<Path> stream = Files.walk(p)) {
                result = (ArrayList<Path>) stream.filter(x -> x.toFile().isFile()).collect(Collectors.toList());
            }
        }

        return result;
    }

    public static boolean createDirectory(String username, String name) throws IOException {
        if(username.isEmpty()) throw new IllegalArgumentException(USERNAME_WARNING);
        if(name.isEmpty()) throw new IllegalArgumentException(PATH_WARNING);

        boolean result = false;

        Path p = Path.of("..", OUTPUT_DIRECTORY,username, name);

        if (!Files.exists(p)) {
            Files.createDirectory(p);
            result = true;
        }
        return result;
    }
}
