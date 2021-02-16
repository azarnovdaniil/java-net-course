package ru.daniilazarnov;

import io.netty.channel.ChannelFutureListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 * Класс содержащий утилитные методы используемые как на стороне сервера, так и на стороне клиента
 */
public class UtilMethod {
    public static final String USER_FOLDER_PATH = Path.of("project", "client", "local_storage")
            .toString() + File.separator;
    public static final String USER = "user1";
    public static final String SERVER_FOLDER_PATH = Path.of("project", "server", "cloud_storage", USER)
            .toString() + File.separator;



    /**
     * Создание каталога по указанному пути
     *
     */
    public static void createFile(String userFolder) throws IOException {
        Path path = Paths.get("cloud_storage", userFolder);
        Files.createDirectories(path.getParent());
        try {
            Files.createFile(path);
        } catch (FileAlreadyExistsException e) {
            System.err.println("already exists: " + e.getMessage());
        }
    }


    public static void deleteFile(String folder) throws IOException {
        Path path = Paths.get("cloud_storage", folder);
//        Files.createDirectories(path.getParent());
        try {
            Files.createDirectories(path);
        } catch (FileAlreadyExistsException e) {
            System.err.println("already exists: " + e.getMessage());
        }
    }




    public static void createFolder(String folder) throws IOException {
        Path path = Paths.get("cloud_storage", folder);
//        Files.createDirectories(path.getParent());
        try {
            Files.createDirectories(path);
        } catch (FileAlreadyExistsException e) {
            System.err.println("already exists: " + e.getMessage());
        }
    }

    /**
     * Получение списка файлов и каталогов по указанному пути
     *
     * @param folderName имя каталога
     * @return результирующая строка имеет вид:  [папка] 'файл';
     */
    public static String getFolderContents(String folderName, String user) throws IOException {
        StringBuilder sb = new StringBuilder();
        String folder;
        if (user.equals("user")) {
            folder = getStringPath(USER_FOLDER_PATH, folderName);
        } else {
            folder = getStringPath(SERVER_FOLDER_PATH, "");
        }
        //папка выделяется квадратной скобкой
        Files.list(Path.of(folder + folderName))
                .map(Path::getFileName)
                .map(Path::toString)
                .map(s -> {
                    if (Files.isDirectory(Path.of(folder + s))) {
                        s = "[" + s + "] "; //папка выделяется квадратной скобкой
                    } else {
                        s = "'" + s + "' ";
                    } // файл выделяется одинарной кавычкой
                    return s;
                })
                .sorted() //сортируем
                .forEach(sb::append); // добавляем в стрингбилдер
        return sb.toString();
    }

    private static String getStringPath(String path, String folderName) {
        String folder;
        folder = path + (folderName.length() > 0 ? folderName + File.separator : "");
        return folder;
    }

    public static ChannelFutureListener getChannelFutureListener(String s) {
        return future -> {
            if (!future.isSuccess()) {
                System.err.println(s + "не был");
            }
            if (future.isSuccess()) {
                System.out.print(s);
            }
        };
    }
}
