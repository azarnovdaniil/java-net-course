package ru.daniilazarnov;

import java.io.*;
import java.util.Arrays;


public class MyCloudFiles {
    private File file;

    public boolean isNewClientDirCreated(String login) {
       file = new File ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\server\\myclaud\\" + login);
        return file.mkdir ();
    }

    public String ls(String login) {
        file = new File ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\server\\myclaud\\" + login);
        return Arrays.toString (file.list ());
    }

    public boolean isFileRenamed(String login, String oldName, String newName) {
        file = new File ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\server\\myclaud\\" + login + "\\" + oldName);
        return file.renameTo (new File ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\server\\myclaud\\" + login + "\\" + newName));
    }

    public boolean isFileDelited(String login, String fileName) {
        file = new File ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\server\\myclaud\\" + login + "\\" + fileName);
        return file.delete ();
    }

    public void sendFileToClient(DataOutputStream out, String login, String fileName) {
        try {
            out = new DataOutputStream(new FileOutputStream ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\client\\desktop\\" + login + "\\" + fileName));

            FileInputStream fis = new FileInputStream ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\server\\myclaud\\" + login + "\\" + fileName);

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
    public boolean isFileExists (String login, String fileName) {
        file = new File ("C:\\Users\\Users\\IdeaProjects\\java-net-course\\project\\server\\myclaud\\" + login + "\\" + fileName);
        return file.exists ();
    }

}