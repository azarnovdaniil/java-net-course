package ru.daniilazarnov;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static DataInputStream dis;
    private static DataOutputStream dos;

    public Client() {
        try {
            Socket socket = new Socket("localhost", 8189);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            sendFile("d:\\test.txt");

            new Thread(()->{
                try {
                    while (true){
                        String msg = dis.readUTF();
                        System.out.println(msg);
                    }
                } catch (IOException e){
                    throw new RuntimeException("SWW in ClientApp during receive message", e);
                }
            }).start();

            Scanner scanner = new Scanner(System.in);
            while (true){
                try {
                    System.out.println("...");
                    dos.writeUTF(scanner.nextLine());

                } catch (IOException e) {
                    throw new RuntimeException("SWW in ClientApp during sending message", e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("SWW in ClientApp", e);
        }
    }

    private static void sendFile(String path) throws Exception{
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        dos.writeLong(file.length());
        byte[] buffer = new byte[4*1024];
        while ((bytes=fileInputStream.read(buffer))!=-1){
            dos.write(buffer,0,bytes);
            dos.flush();
        }
        fileInputStream.close();
    }

    public static void main(String[] args) {
        new Client();
    }
}
