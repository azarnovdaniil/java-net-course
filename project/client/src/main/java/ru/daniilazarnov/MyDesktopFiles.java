package ru.daniilazarnov;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class MyDesktopFiles {
    private File file;
    private Client client;
    public Scanner sc;



    public boolean isNewClientDirCreated(String login) {
        File fileTest = new File ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\client\\desktop\\" + login);
        return fileTest.mkdir ();
    }

    public boolean isFileCreatedAtDesktop(String login) throws IOException {
        sc = new Scanner (System.in);
        System.out.println ("Введите название файла в формате .txt");
        String fileName = sc.nextLine ();
        file = new File ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\client\\desktop\\" + login + "\\" + fileName);
        sc.close ();
        return file.createNewFile ();
    }

    public void downloadFileToCloud (DataOutputStream out, String login) {
        try {
            sc = new Scanner (System.in);
            System.out.println ("Введите название файла в формате .txt");
            String fileName = sc.nextLine ();
            out = new DataOutputStream(new FileOutputStream ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\server\\myclaud\\" + login + "\\" + fileName));

            FileInputStream fis = new FileInputStream ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\client\\desktop\\" + login + "\\" + fileName);

            byte[] byteArray = new byte[16*1024];
            int count;
            while ((count = fis.read(byteArray)) != -1){
                out.write(byteArray,0,count);
            }
            fis.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace ();

        }
    }

    public boolean isFileRenamed(String login, String oldName, String newName) {
        file = new File ("mycloud/_" + login + "/" + oldName);
        return file.renameTo (new File ("mycloud/_" + login + "/" + newName));
    }

    public boolean isFileDelited(String login, String fileName) {
        file = new File ("mycloud/_" + login + "/" + fileName);
        return file.delete ();
    }



}