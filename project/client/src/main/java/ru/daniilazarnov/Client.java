package ru.daniilazarnov;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    static Socket socket;
    final static String IP_ADDRESS = "localhost";
    final static int PORT = 8189;
    static String clientMsg;

    public static void main(String[] args) {

        try {
            socket = new Socket(IP_ADDRESS, PORT);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            Scanner in = new Scanner(socket.getInputStream());
            out.write(new byte[]{115, 21, 31}); //Почему
            clientMsg = in.nextLine();
            System.out.println("A: " + clientMsg);
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
