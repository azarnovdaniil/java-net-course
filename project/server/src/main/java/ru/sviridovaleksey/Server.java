package ru.sviridovaleksey;

import ru.sviridovaleksey.newClientConnection.ClientConnection;

import java.io.IOException;
import java.util.logging.*;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private static Handler fileHandler;
    private static final int defaultPort = 8189;

    public static void main(String[] args) {
        int usePort = defaultPort;

        if (args.length != 0) {
            usePort = Integer.parseInt(args[0]);
        }

        try {
            fileHandler = new FileHandler("log_%g.txt", 10*1024, 20, true);
            fileHandler.setFormatter(new SimpleFormatter());

            new ClientConnection(fileHandler, usePort).startConnection();
        } catch (IOException e) {
            logger.log(Level.SEVERE,e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
