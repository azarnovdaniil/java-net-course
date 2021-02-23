package ru.johnnygomezzz.commands;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ListCommand {
    private static final String PATH_LOCAL = ("project/client/local/");

    public void listCommand(String dir) {
        File dirs = new File(PATH_LOCAL, dir);
        File[] arrFiles = dirs.listFiles();

        List<File> list = null;

        if (arrFiles != null) {
            list = Arrays.asList(arrFiles);
        } else {
            System.out.println("Каталог пуст.");
        }
        System.out.println(list);
    }
}
