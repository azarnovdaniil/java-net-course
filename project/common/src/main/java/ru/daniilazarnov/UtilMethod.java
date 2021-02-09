package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Collectors;


public class UtilMethod {
    public static final String HOME_FOLDER_PATH = "project\\client\\local_storage\\";

    public static void createDirectory(String filePath) throws IOException {
        Path path = Paths.get("project/server/cloud_storage/user1/" + filePath);
        Files.createDirectories(path.getParent());
        try {
            Files.createFile(path);
        } catch (FileAlreadyExistsException e) {
            System.err.println("already exists: " + e.getMessage());
        }
    }

    public static String getFolderContents(String folderName) throws IOException {
        StringBuilder sb = new StringBuilder();
        String folder = HOME_FOLDER_PATH + (folderName.length() > 0 ? folderName + File.separator : "");
        Files.list(Path.of(HOME_FOLDER_PATH + folderName))
                .map(Path::getFileName)
                .forEach(path -> {
                    if (Files.isDirectory(Path.of(folder + path))) {
                        sb.append("[").append(path).append("] "); //папка выделяется квадратной скобкой
                    } else sb.append("'").append(path).append("' "); // файл выделяется одинарной кавычкой
                });
        return sb.toString();
    }


    public  static String receiveAndEncodeString(ByteBuf buf) {
        int msgLength = (byte) buf.readInt();
        byte[] messageContent = new byte[msgLength];
        buf.readBytes(messageContent);
        return new String(messageContent);
    }


    @NotNull
    public static  ChannelFutureListener getChannelFutureListener(String s) {
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
