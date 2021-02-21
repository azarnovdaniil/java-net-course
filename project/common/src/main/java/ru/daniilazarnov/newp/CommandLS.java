package ru.daniilazarnov.newp;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class CommandLS implements Serializable {
    private String path;

    public CommandLS(String path) {
        this.path = path;
    }

    public ArrayList<String> listFiles() {
        File dir = new File(path);
        File[] files = dir.listFiles();
        ArrayList<String> list = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    list.add(String.format("[%s] size: %s", file.getName(), Utils.bytesConverter(file.length())));
                }
            }
        }
        return list;
    }
}