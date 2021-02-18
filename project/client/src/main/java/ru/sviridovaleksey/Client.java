package ru.sviridovaleksey;

import ru.sviridovaleksey.network.Connection;

public class Client {

    private static final String SERVERADDRESS = "localHost";
    private static final int SERVERPORT = 8189;


    public static void main(String[] args) {

        int usePort = SERVERPORT;

        if (args.length != 0) {
            usePort = Integer.parseInt(args[0]);
        }
        new Connection(SERVERADDRESS, usePort);
    }
}
