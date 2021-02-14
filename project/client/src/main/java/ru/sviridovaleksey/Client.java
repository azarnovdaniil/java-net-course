package ru.sviridovaleksey;

import ru.sviridovaleksey.network.Connection;

public class Client {

    private static final String serverAddress = "localHost";
    private static  int serverPort = 8189;


    public static void main(String[] args) {

        if (args.length != 0) {
            serverPort = Integer.parseInt(args[0]);
        }

        new Connection(serverAddress, serverPort);


    }
}