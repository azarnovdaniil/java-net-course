package ru.daniilazarnov;

import io.netty.channel.ChannelFutureListener;
//import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 * Класс содержащий утилитные методы используемые как на стороне сервера, так и на стороне клиента
 */
public class UtilMethod {
    public static final String USER_FOLDER_PATH = "project\\client\\local_storage\\";
    public static final String SERVER_FOLDER_PATH = "project\\server\\cloud_storage\\user1\\";
    public static final String user = "user1";
//                                                   project/server/cloud_storage

    /**
     * Создание каталога по указанному пути
     *
     * @param filePath путь содержащий имя файла
     */
    public static void createDirectory(String filePath) throws IOException {
        Path path = Paths.get("project/server/cloud_storage/user1/" + filePath);
        Files.createDirectories(path.getParent());
        try {
            Files.createFile(path);
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
//        Files.list(Path.of(USER_FOLDER_PATH + folderName))
        Files.list(Path.of(folder + folderName))
                .map(Path::getFileName)
                .map(Path::toString)
                .map(s -> {
                    if (Files.isDirectory(Path.of(folder + s))) {
                        s = "[" + s + "] "; //папка выделяется квадратной скобкой
                    } else s = "'" + s + "' "; // файл выделяется одинарной кавычкой
                    return s;
                })
                .sorted() //сортируем
                .forEach(sb::append); // добавляем в стрингбилдер
        return sb.toString();
    }

//    @NotNull
    private static String getStringPath(String path, String folderName) {
        String folder;
        folder = path + (folderName.length() > 0 ? folderName + File.separator : "");
        return folder;
    }

//    /**
//     * Принимает строку по протоколу   * Формирует для отправки на сервер имя файла по протоколу
//     * * [][][][] int  = длинна имени файла;
//     * * [] byte[] - имя файла;
//     */
//    public static String receiveAndEncodeString(ByteBuf buf) {
//        int msgLength = (byte) buf.readInt();
//        byte[] messageContent = new byte[msgLength];
//        buf.readBytes(messageContent);
//        return new String(messageContent);
//    }


//    @NotNull
    public static ChannelFutureListener getChannelFutureListener(String s) {
        return future -> {
            if (!future.isSuccess()) {
                System.err.println(s + "не был");
//                log.error(future.cause());

//                    future.cause().printStackTrace();
            }
            if (future.isSuccess()) {
                System.out.print(s);
//                    channel.disconnect();
//                    channel.close();
//                    Network.getInstance().stop();
            }
        };
    }


}