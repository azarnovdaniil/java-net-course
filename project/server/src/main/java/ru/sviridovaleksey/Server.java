package ru.sviridovaleksey;

import ru.sviridovaleksey.newclientconnection.ClientConnection;

import java.io.IOException;
import java.util.logging.*;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static final int LIMITLOGGER = 10 * 1024;
    private static final int COUNTLOGGER = 20;
    private static final int DEFAULTPORT = 8189;

    public static void main(String[] args) {
        int usePort = DEFAULTPORT;

        if (args.length != 0) {
            usePort = Integer.parseInt(args[0]);
        }

        try {
            Handler fileHandler = new FileHandler("log_%g.txt", LIMITLOGGER, COUNTLOGGER, true);
            fileHandler.setFormatter(new SimpleFormatter());

            new ClientConnection(fileHandler, usePort).startConnection();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
