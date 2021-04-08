package ru.daniilazarnov.command;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class StorageCommand {
    private final String STORAGE = "C:\\storage\\";

    protected void createFile(String name) {
        System.out.println(name); // лог
        File file = new File(STORAGE + name);
        try {                                               // *** здесь обрабатывать t/c или пробрасывать выше ?
            if (file.createNewFile()) {
                System.out.println("successful create file: " + file.getName()); // send client
            } else {
                System.out.println("unsuccessful create file " + file.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void createDir (String name) {
        System.out.println(name); // лог
        File file = new File(STORAGE + name);
        if (file.mkdir()) {
            System.out.println("successful create dir: " + file.getName()); // send client
        } else {
            System.out.println("unsuccessful create dir " + file.getName());
        }
    }

    protected void rename(String oldName, String newName) {
        System.out.println(oldName + " " + newName); // лог
        File file = findItem(oldName);
        File newFile = new File(file.getParent() + "\\" + newName);
        if (file.renameTo(newFile)) {
            System.out.println("successful rename file: " + oldName + " in " + newName); // send client
        } else {
            System.out.println("unsuccessful rename file " + oldName);
        }
    }

    protected void delete(String name) {
        System.out.println(name); // лог
        if (FileUtils.deleteQuietly(findItem(name))) {
            System.out.println("successful delete file: " + name); // send client
        } else {
            System.out.println("unsuccessful delete file: " + name);
        }
    }

    private File findItem(String name) {
        File dir = new File(findDir(name));
        File file = new File(STORAGE + name);
        if (!dir.isDirectory()) {
            String str = dir.getParent();
            dir = new File(str);
        }

        Collection<File> files = FileUtils.listFilesAndDirs(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File item : files) {
            if (item.getName().equals(file.getName())) {
                file = item;
                break;
            }
        }
        return file;
    }

    private String findDir(String name) {
        StringBuilder dir = new StringBuilder(STORAGE);
        char[] arr = name.toCharArray();
        for (char c : arr) {
            if (c == '\\') {
                dir.append(name);
                break;
            }
        }
        return dir.toString();
    }
}
