package ru.daniilazarnov;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class StorageServer {
    private static final Logger logger = Logger.getLogger(StorageServer.class.getName());

    private static final int PORT = 8894;
    private ExecutorService executorService;
    private ServerSocket serverSocket;

    public StorageServer () {
        this.loggerSettings();

        try {
            this.serverSocket = new ServerSocket(PORT);
            this.executorService = Executors.newCachedThreadPool();
            this.executorService.execute(new StorageServerHandler(this, this.serverSocket, this.executorService));
            this.logger.log(Level.INFO, "StorageServer started!");
        } catch (Exception e) {
            this.logger.log(Level.WARNING, "StorageServer started IOException");
        }

    }


    public static void loggerSettings () {
        try {
            Path properties = Paths.get("logging.properties");
            Path logDir = Paths.get("log");

            if(Files.exists(properties)) {
                LogManager.getLogManager().readConfiguration(new FileInputStream(properties.toString()));
            }

            if (!Files.isDirectory(logDir)) Files.createDirectory(logDir);

            logger.addHandler(new FileHandler("log/log_%g.txt", 10 * 1024, 20, true));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
