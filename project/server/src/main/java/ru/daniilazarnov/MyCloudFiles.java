package ru.daniilazarnov;

import java.io.*;
import java.util.Arrays;

public class MyCloudFiles {
    private File file;

    public boolean isNewClientDirCreated(String login) {
       file = new File ("mycloud/_" + login);
        return file.mkdir ();
    }

    public String ls(String login) {
        file = new File ("mycloud/_" + login);
        return Arrays.toString (file.list ());
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
//                bos.write (bis.read ());
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