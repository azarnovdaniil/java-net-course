package ru.daniilazarnov;

import java.io.IOException;
import java.util.logging.Logger;

public class ServerApp {
    static Logger logger = Logger.getLogger(String.valueOf(ServerApp.class));

    public ServerApp(){
        logger.info("INFO");
        System.out.println("ServerApp started");

        try {
            Server server = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
