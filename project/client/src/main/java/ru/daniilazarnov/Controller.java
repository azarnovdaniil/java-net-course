package ru.daniilazarnov;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller {
    private static Client2 client;

    private static String msg = "◙◙◙";

    public static void sendMsg(String message) {
        client.sendMessage(message);
    }

    public static void main(String[] args) throws IOException {
        client = new Client2();
        InputStream in = System.in;


        BufferedReader bufferedReader = null;
        String inputLine = "";
        while (!inputLine.equals("exit")) {
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            System.out.println("Введите сообщение:");
            inputLine = bufferedReader.readLine().trim();
            sendMsg(inputLine);
        }

        bufferedReader.close();


    }



}