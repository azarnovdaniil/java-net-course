package ru.daniilazarnov;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {public static void main(String[] args) {
    System.out.println("Client!");


    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    try {
        Socket socket = new Socket("localhost", 8189);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
        out.write(new byte[]{21, 21, 21});
        String x = reader1.readLine();
        System.out.println("A: " + x);
        reader1.close();
        out.close();
        socket.close();
    } catch (Exception e) {
        e.printStackTrace();
    }


}
}
