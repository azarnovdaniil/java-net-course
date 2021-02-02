package com.geekbrains.dbox.client;

import java.io.*;

public class Client {
    public static void main(String[] args) throws InterruptedException, IOException {
        Network network = new Network();


        InputStream inputStream = System.in;
        Reader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        boolean t = true;
        while (t) {
            String name = bufferedReader.readLine(); //читаем строку с клавиатуры
            network.sendMessage(name);
            if (name.equals("exit")) {
                t = false;
                network.close();
            }
        }

    }

}
