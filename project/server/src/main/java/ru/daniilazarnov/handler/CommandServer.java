package ru.daniilazarnov.handler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommandServer {
    public CommandServer (){}
    protected StringBuilder showFiles (Path path){
        File dir = new File(String.valueOf(path));
        StringBuilder sb = new StringBuilder();

        File[] files = dir.listFiles();
        sb.append(" For user: user " + "\n");
        for (File file : files) {
            long lastModified = file.lastModified();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            sb.append(file.getName() + " ,date of change " + sdf.format(new Date(lastModified))+"\n");

        }
        return sb;
    }
    protected StringBuilder callHelpManual() {
        File f = new File("project/server/src/help.txt");
        BufferedReader fin = null;
        try {
            fin = new BufferedReader(new FileReader(f));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = fin.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            return sb;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    protected void deleteFile(String address) {
        try {
            Files.delete(Path.of(address));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void renameFile(String lastName, String newName) {
        File file = new File(lastName);
        File newFile = new File(newName);
        file.renameTo(newFile);

    }

}
