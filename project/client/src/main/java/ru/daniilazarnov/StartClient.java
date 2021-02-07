package ru.daniilazarnov;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class StartClient {
    private static final Logger logger = Logger.getLogger(StartClient.class.getName());
    private final static String serverfile = "project/server/files/server.txt";

    public static void main(String[] args) {
        Network network = new Network();
        if (!network.connect()) {
            logger.info("Ошибка подключения к серверу");
        }

        network.waitMessage();
    }

}
