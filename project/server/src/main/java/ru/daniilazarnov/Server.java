package ru.daniilazarnov;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private Socket socket;
    private static DataInputStream dis;
    private static DataOutputStream dos;
//    private FileInputStream fis;

    public Server() {
        try {
            serverSocket = new ServerSocket(8189);
            System.out.println("Server is up");

            while (true){
                System.out.println("Server is listening port 8189...");
                socket = serverSocket.accept();
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                System.out.println("Client connected: " + socket);
                dos.writeUTF("Welcome to chat!");

                receiveFile("test.txt");

                dis.close();
                dos.close();
                socket.close();
            }
        } catch (Exception e){
            throw new RuntimeException("SWW during loading server", e);
        }
    }

    private static void receiveFile(String fileName) throws Exception{
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        long size = dis.readLong();
        byte[] buffer = new byte[4096];
        while (size > 0 && (bytes = dis.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0, bytes);
            size -= bytes;
        }
        fileOutputStream.close();
    }

    public static void main(String[] args) {
        new Server();
    }
}
