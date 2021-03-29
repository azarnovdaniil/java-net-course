package ru.daniilazarnov.hello;

import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class IoClient {

    public static final int PORT = 8189;
    public static final String HOST = "localhost";

    public static void main(String[] args) {
        new IoClient().run();
    }

    void run() {
        Scanner scanner = new Scanner(System.in);

        String input = scanner.next();

        try (Socket socket = new Socket(HOST, PORT);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner in = new Scanner(socket.getInputStream())) {

            out.write(input.getBytes(StandardCharsets.UTF_8));

            while (in.hasNext()) {
                String output = in.next();
                System.out.println("Received message: " + output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
