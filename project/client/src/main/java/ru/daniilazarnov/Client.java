package ru.daniilazarnov;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {

    /*
    Нереализованный протокол:
            [byte  ] 1b управляющий байт →
            [short ] 2b длинна имени файла →
            [byte[]] nb  имя файла →
            [long  ] 8b размер файла →
            [byte[]] nb содержимое файла
     */
    private static final Logger log = Logger.getLogger(Client.class);

    private static Socket socket;
    private static DataOutputStream out;
    private static Scanner in;
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 8189;


    public static void main(String[] args) throws IOException {
        InputStream input = System.in;
        String inputString;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input))) {
            inputString = bufferedReader.readLine().trim();
        }


        try {
            out = connect();
            sendMsg();

            String x = in.nextLine();
            System.out.println("A: " + x);
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void sendMsg() throws IOException {
        out.write(new byte[]{11, 21, 31});
    }

    @NotNull
    private static DataOutputStream connect() throws IOException {
        socket = new Socket(HOSTNAME, PORT);
        in =  new Scanner(socket.getInputStream());

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        return out;
    }

}
