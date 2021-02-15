package ru.daniilazarnov;

import io.netty.channel.ChannelFutureListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class UtilMethod {
    public static final String USER_FOLDER_PATH = String.valueOf(Paths.get("project","client", "local_storage"));
    public static final String SERVER_FOLDER_PATH = String.valueOf(Paths.get("project","server","cloud_storage","testUser"));
    public static final String USER = "testUser";

    public static void createDirectory(String filePath) throws IOException {
        Path path = Paths.get("project/server/cloud_storage/user1/" + filePath);
        Files.createDirectories(path.getParent());
        try {
            Files.createFile(path);
        } catch (FileAlreadyExistsException e) {
            System.err.println("already exists: " + e.getMessage());
        }
    }

    public static String getFolderContents(String folderName, String user) throws IOException {
        StringBuilder sb = new StringBuilder();
        String folder;
        if (user.equals("user")) {
            folder = getStringPath(USER_FOLDER_PATH, folderName);
        } else {
            folder = getStringPath(SERVER_FOLDER_PATH, "");
        }

        Files.list(Path.of(folder + folderName))
                .map(Path::getFileName)
                .map(Path::toString)
                .map(s -> {
                    if (Files.isDirectory(Path.of(folder + s))) {
                        s = "[" + s + "] ";
                    } else s = "'" + s + "' ";
                    return s;
                })
                .sorted()
                .forEach(sb::append);
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
