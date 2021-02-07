package ru.daniilazarnov;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class StartServer {
    private static final int DEFAULT_PORT = 8190;
    private static final Logger logger = Logger.getLogger("");

    public static void main(String[] args) {

        Handler fileHandler = null;
        try {
            fileHandler = new FileHandler("project/logs/server.log", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fileHandler);
        fileHandler.setFormatter(new SimpleFormatter());

        int port = DEFAULT_PORT;
        if(args.length != 0) {
            port = Integer.parseInt(args[0]);
        }

        try {
            new Server(port).start();
        }
        catch (IOException e) {
            System.out.println("Ошибка!");
            e.printStackTrace();
            System.exit(1);
        }


    }
}
