package ru.daniilazarnov;


import java.io.*;
import java.net.*;

public class Server {

    private final static String fileToSend = "C:\\java_log\\1\\test_in.txt";

    public static void main(String args[]) {
        System.out.println("Сервер запущен, ожидаем передачи файла...");
        while (true) {
            ServerSocket ServSocket;
            Socket clientSocket = null;
            BufferedOutputStream outToClient = null;

            try {
                ServSocket = new ServerSocket(8189);
                clientSocket = ServSocket.accept();
                outToClient = new BufferedOutputStream(clientSocket.getOutputStream());
            } catch (IOException ex) {

                          }

            if (outToClient != null) {
                File myFile = new File( fileToSend );
                FileInputStream fis = null;
                byte[] byteArray = new byte[(int) myFile.length()];

                try {
                    fis = new FileInputStream(myFile);
                } catch (FileNotFoundException ex) {

                }
                BufferedInputStream bis = new BufferedInputStream(fis);

                try {
                    bis.read(byteArray, 0, byteArray.length);
                    outToClient.write(byteArray, 0, byteArray.length);
                    outToClient.flush();
                    outToClient.close();
                    clientSocket.close();
                    System.out.println("Файл принят, закрываем соединение...");

                    return;
                } catch (IOException ex) {
                    System.out.println("Что-то пошло не так...");
                }
            }
        }
    }
}
