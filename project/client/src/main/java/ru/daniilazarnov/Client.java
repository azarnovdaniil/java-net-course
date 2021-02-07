package ru.daniilazarnov;

import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;


public class Client {
    static Socket socket;
    final static String IP_ADDRESS = "localhost";
    final static int PORT = 8189;
    static String clientMsg;
    static final String HOME_FOLDER_PATH= "project/client/local_storage/";

    public static void main(String[] args) {




        try {
            socket = new Socket(IP_ADDRESS, PORT);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            Scanner in = new Scanner(socket.getInputStream());

//            out.write(new byte[]{115, 21, 31});
            clientMsg = in.nextLine().toLowerCase();

            String[] token = clientMsg.split("\\s", 2);

            if(token[1].equals("fls")){
                System.out.println(getFileList(token[1]));
            }

            switch (token[2]){
                case "dlf":
                    downloadFilesFromServerDirectory(token[2]);
                    break;
//                case "ulf":
//                    uploadFileInServerDirectory(token[2]);
//                    break;
                case "exit":
                    System.out.println("Program closing");
                    in.close();
                    out.close();
                    break;
            }


            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void downloadFilesFromServerDirectory(String fileName) {
        /*
        Здесь будет код ля скачивания файла из директории в server`e в директорию client/local_storage
         */
        if(!isFileExists(fileName)){
            System.out.println("File not found");
        }
    }

    private static String getFileList(String clientMsg) {
        String fileList = "empty";
        String fileName;

        return fileList;
    }

    private static boolean isFileExists(String fileName) {
        return Files.exists(Path.of(HOME_FOLDER_PATH + fileName));
    }

}
