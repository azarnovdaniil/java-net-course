package ru.daniilazarnov;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        System.out.println("Client!");
        try {
            Socket socket = new Socket("Localhost", 8888);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    while (true) {
                        String message = in.readUTF();
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Something went wrong ...", e);
                }
            }).start();

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                try {
                    System.out.println("...");
                    out.writeUTF(br.readLine());
                } catch (IOException e) {
                    throw new RuntimeException("Something went wrong ...", e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
