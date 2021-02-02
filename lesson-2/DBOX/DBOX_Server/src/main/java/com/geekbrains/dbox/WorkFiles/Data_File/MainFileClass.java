package com.geekbrains.dbox.WorkFiles.Data_File;

import java.io.*;

public class MainFileClass {

    protected String pathNameFile;
    protected MainFileClass(String pathNameFile){
        this.pathNameFile = pathNameFile;
    }

// Проверяет существует ли файл, если нет то создает его
    protected void fileExists(File f) throws IOException {
        if (!f.exists()) {
            if (!f.createNewFile()) System.out.println("Файл (" + f.toPath() + ") создать не получилось");
        }
    }
// Метод определения количества строк в файле
    public static int countLinesOld(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }
}
