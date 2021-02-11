package ru.daniilazarnov;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class MyDesktopFiles {
    private File file;
    private Client client;
    Scanner sc;

    public boolean isNewClientDirCreated(String login) {
        File fileTest = new File ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\client\\desktop\\_" + login);
        return fileTest.mkdir ();
    }

    public boolean isFileCreatedAtDesktop(String login) throws IOException {
        sc = new Scanner(System.in);
        System.out.println ("Введите название файла в формате .txt");
        String fileName = sc.nextLine ();
        file = new File ("desktop/_" + login + "/" + fileName);
        return file.createNewFile ();
    }

    public void ls(String login) {
        String msg = String.format("/ls %s", login);
        client.sendMsg (msg);
    }

    public boolean isFileRenamed(String login, String oldName, String newName) {
        file = new File ("mycloud/_" + login + "/" + oldName);
        return file.renameTo (new File ("mycloud/_" + login + "/" + newName));
    }

    public boolean isFileDelited(String login, String fileName) {
        file = new File ("mycloud/_" + login + "/" + fileName);
        return file.delete ();
    }

    public void sendFileToClient(DataOutputStream out, String login, String fileName) {
        try {
            out = new DataOutputStream(new FileOutputStream ("desktop/_" + login + "/" + fileName));

            FileInputStream fis = new FileInputStream ("mycloud/_" + login + "/" + fileName);

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

    public void receiveFileFromClient(DataInputStream in, String login, String fileName) {
        try {
            FileOutputStream fot = new FileOutputStream ("mycloud/_" + login + "/" + fileName);

            byte[] byteArray = new byte[16*1024];
            int count;
            while ((count = in.read(byteArray)) != -1){
                fot.write(byteArray,0,count);
            }
            in.close();
            fot.close();

        } catch (IOException e) {
            e.printStackTrace ();

        }
    }


}