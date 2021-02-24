package ru.sviridovaleksey;

import ru.sviridovaleksey.newclientconnection.ClientConnection;

import java.io.File;
import java.net.URLDecoder;
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
            String path = Server.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String defaultAddress = URLDecoder.decode(path, "UTF-8");
            String pathForLog = new File(defaultAddress).getParent();
            System.out.println(pathForLog + "\\" + "logServer_%g.txt");
            Handler fileHandler = new FileHandler(pathForLog + "\\" + "logServer_%g.txt",
                    LIMITLOGGER, COUNTLOGGER, true);
            fileHandler.setEncoding("UTF-8");
            fileHandler.setFormatter(new SimpleFormatter());
            new ClientConnection(fileHandler, usePort).startConnection(defaultAddress);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

    }
}

