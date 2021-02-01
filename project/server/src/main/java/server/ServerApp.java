package server;

import server.chat.MyServer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.*;

public class ServerApp {
    private static final int SERVER_PORT = 8189;
    private static Logger logger = Logger.getLogger("");

    public static void main(String[] args) {
        logger.setLevel(Level.INFO);
        try {
//            Handler fileHandler = new FileHandler("log/log_%g.log", 512 * 1024, 4, true);
//            fileHandler.setFormatter(new SimpleFormatter());
//            logger.addHandler(fileHandler);
            LogManager manager = LogManager.getLogManager();
            manager.readConfiguration(new FileInputStream("log/logging.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int port = SERVER_PORT;

        if (args.length != 0) {
            port = Integer.parseInt(args[0]);
        }

        try {
            new MyServer(port).start();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Ошибка запуска сервера");
//            System.out.println("Ошибка запуска сервера");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
