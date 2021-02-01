package ru.daniilazarnov;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
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

    private ServerSocketChannel serverSocketChannel;
    private ExecutorService executorService;
    private Selector selector;


    public static final String LOCATION_FILES = "files" + File.separator;
    public static final String LOCATION_TEMP_FILES = LOCATION_FILES + File.separator + "temp" + File.separator;

    public static final int PORT = 8894;

    public StorageServer () {

        this.loggerSettings();
        this.executorService = Executors.newCachedThreadPool();

        try {

            this.serverSocketChannel = ServerSocketChannel.open();
            this.serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            this.serverSocketChannel.configureBlocking(false);
            this.selector = Selector.open();
            this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            this.executorService.execute(new StorageServerHandler(this));

            this.logger.log(Level.INFO, "StorageServer started!");
        } catch (Exception e) {
            this.logger.log(Level.WARNING, "StorageServer started IOException");
        }

    }

    public ExecutorService getExecutorService () {
        return this.executorService;
    }

    public Selector getSelector () {
        return this.selector;
    }

    public ServerSocketChannel getServerSocketChannel () {
        return this.serverSocketChannel;
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
