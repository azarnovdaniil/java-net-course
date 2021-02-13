package ru.daniilazarnov.pipeline;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MyClient {

    public static final int PORT = 8189;
    public static final String LOCALHOST = "localhost";
    public static final int MAGIC_NUMBER = 21;

    public static void main(String[] args) {
        try (Socket socket = new Socket(LOCALHOST, PORT);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner in = new Scanner(socket.getInputStream())) {

            out.write(new byte[]{MAGIC_NUMBER, MAGIC_NUMBER, MAGIC_NUMBER});
            String x = in.nextLine();
            System.out.println("A: " + x);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
