package ru.sviridovaleksey;

import ru.sviridovaleksey.interactionwithuser.HelloMessage;
import ru.sviridovaleksey.interactionwithuser.Interaction;
import ru.sviridovaleksey.network.Connection;

public class Client {

    private static final String serverAddress = "localHost";
    private static final int serverPort = 8189;


    public static void main(String[] args) {

        new Connection(serverAddress, serverPort);


    }
}