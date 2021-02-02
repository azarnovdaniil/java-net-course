package ru.daniilazarnov;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final static String fileToSend = "C:\\temp\\1.txt";
    private static final int PORT = 8190;

    public static void main(String args[]) {

        while (true) {
            ServerSocket welcomeSocket = null;
            Socket connectionSocket = null;
            BufferedOutputStream outToClient = null;
            System.out.println("Сервер запущен");
            try {
                welcomeSocket = new ServerSocket(PORT);
                connectionSocket = welcomeSocket.accept();
                outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
            } catch (IOException ex) {
                System.out.println("У сервера возникли проблемы с открытием сокета...");
            }
            System.out.println("Готов принимать подключения...");

            if (outToClient != null) {
                System.out.println("Установлено подключение, готов отдавать файл...");
                File myFile = new File( fileToSend );
                byte[] mybytearray = new byte[(int) myFile.length()];

                FileInputStream fis = null;

                try {
                    fis = new FileInputStream(myFile);      // считали в поток данные из файла, лежащего на сервере
                } catch (FileNotFoundException ex) {
                    // Do exception handling
                }
                BufferedInputStream bis = new BufferedInputStream(fis);

                try {
                    bis.read(mybytearray, 0, mybytearray.length);
                    outToClient.write(mybytearray, 0, mybytearray.length);
                    outToClient.flush();
                    outToClient.close();
                    connectionSocket.close();

                    // File sent, exit the main method
                    return;
                } catch (IOException ex) {
                    System.out.println("Ошибка при получении");
                }
            }
        }
    }
}
