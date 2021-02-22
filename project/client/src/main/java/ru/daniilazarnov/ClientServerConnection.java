package ru.daniilazarnov;

import javafx.application.Platform;
import ru.daniilazarnov.CommandsType.DeleteFilesCommandData;
import ru.daniilazarnov.CommandsType.ErrorCommandData;
import ru.daniilazarnov.CommandsType.FileMessage;


import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public class ClientServerConnection {

    private static Socket socket;
    private static ObjectInputStream dataInputStream;
    private static ObjectOutputStream dataOutputStream;


    public static void startConnect(){
        try{
            Socket socket = new Socket("localhost", 8189);
            dataInputStream = new ObjectInputStream(socket.getInputStream());
            dataOutputStream = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private Commands executeCommands() throws IOException{

        try{
            return (Commands) dataInputStream.readObject();
        } catch (ClassNotFoundException e){
            String errorMessage = "Incorrect data!";
            System.err.println(errorMessage);
            e.printStackTrace();
            sendMessage(Commands.errorCommand(errorMessage));
            return null;
        }
    }

    private void sendMessage(Commands commands) throws IOException{

        dataOutputStream.writeObject(commands);
    }



    public static void stopConnection() {
        try {
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean deletionFiles (String login, LinkedList<File> fileNameToDelete) {
        try {
            if (!fileNameToDelete.isEmpty()) {
                dataOutputStream.writeObject(new DeleteFilesCommandData(login, fileNameToDelete));
                dataOutputStream.flush();
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean uploadFiles(String login, LinkedList<File> filesToSendToCloud) {
        try {
            if (!filesToSendToCloud.isEmpty()) {
                for (int i = 0; i < filesToSendToCloud.size(); i++) {
                    Path path = Paths.get(filesToSendToCloud.get(i).getAbsolutePath());
                    dataOutputStream.writeObject(new FileMessage(login, path));
                    dataOutputStream.flush();
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
