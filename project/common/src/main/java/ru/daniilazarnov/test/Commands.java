package ru.daniilazarnov.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Commands {

//    public void listFiles(String directory, TestCC controller) throws IOException {
//        File dir = new File(directory);
//        File[] files = dir.listFiles();
//        if (files != null && files.length > 0) {
//            for (File file : files) {
//                if (!file.isDirectory()) {
//                    controller.command(String.format("[%s] size: [%s]", file.getName(), Utils.bytesConverter(file.length())));
//                }
//            }
//        }
//    }

    public void listFiles(String directory, TestCC controller) throws IOException {
        File dir = new File(directory);
        File[] files = dir.listFiles();
        ArrayList<String> list = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    list.add(String.format("[%s] size: %s", file.getName(), Utils.bytesConverter(file.length())));
                }
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(list);
        byte[] bytes = bos.toByteArray();
        controller.command(bytes);
    }
}
