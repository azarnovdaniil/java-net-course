package ru.daniilazarnov;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ru.daniilazarnov.Common.*;

public class Client {
    public static final int MAGIC_NUMBER = 21;

    public static void main(String[] args) {
        System.out.println("Client!");

//        IoClientOne();

        IoClientTwo();
    }

    private static void IoClientTwo() {
        try (Socket socket = new Socket(LOCALHOST, PORT);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner in = new Scanner(socket.getInputStream())) {

            out.write(new byte[]{MAGIC_NUMBER, MAGIC_NUMBER, MAGIC_NUMBER});
            String x = in.nextLine();
            System.out.println("A: " + x);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void IoClientOne() {
        Scanner scanner = new Scanner(System.in);

        String input = scanner.next();

        try (Socket socket = new Socket(LOCALHOST, PORT);
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
