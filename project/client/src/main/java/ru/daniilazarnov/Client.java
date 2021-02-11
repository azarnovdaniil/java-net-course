package ru.daniilazarnov;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        System.out.println("Client!");
        try (Socket socket = new Socket("localhost", 8888);
                BufferedWriter writer =
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        socket.getOutputStream()));
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()))
                )
        {
            System.out.println("Connected to server");

                Scanner scanner = new Scanner(System.in);
                String request = scanner.next();
                writer.write(request);
                writer.newLine();
                writer.flush();
                String response = reader.readLine();
                System.out.println(response);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
