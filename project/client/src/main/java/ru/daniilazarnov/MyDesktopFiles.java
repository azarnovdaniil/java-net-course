package ru.daniilazarnov;

import java.io.*;
import java.util.Scanner;

public class MyDesktopFiles {
    public Scanner sc;



    public boolean isNewClientDirCreated(String login) {
        File file = new File ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\client\\desktop\\" + login);
        return file.mkdir ();
    }

    public boolean isFileCreatedAtDesktop(String login) throws IOException {
        sc = new Scanner (System.in);
        System.out.println ("Введите название файла в формате .txt");
        String fileName = sc.nextLine ();
        File file = new File ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\client\\desktop\\" + login + "\\" + fileName);
        return file.createNewFile ();
    }

    public void downloadFileToCloud (DataOutputStream out, String login) {
        try {
            sc = new Scanner (System.in);
            System.out.println ("Введите название файла в формате .txt");
            String fileName = sc.nextLine ();
            if (isFileExists (login,fileName)){
                out = new DataOutputStream(new FileOutputStream ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\server\\myclaud\\" + login + "\\" + fileName));

                FileInputStream fis = new FileInputStream ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\client\\desktop\\" + login + "\\" + fileName);

                byte[] byteArray = new byte[16*1024];
                int count;
                while ((count = fis.read(byteArray)) != -1){
                    out.write(byteArray,0,count);
                }
                fis.close();
                out.close();
                System.out.println ("Файл загружен.");
            } else System.out.println ("Нет файла с таким именем.");

        } catch (IOException e) {
            System.out.println ("Ошибка загрузки файла.");
        }
    }
    public boolean isFileExists (String login, String fileName) {
        File file = new File ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\client\\desktop\\" + login + "\\" + fileName);
        return file.exists ();
    }
}