package ru.daniilazarnov;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StorageServerHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(StorageServerHandler.class.getName());

    private StorageServer storageServer;
    private Socket socket;
    private ServerSocket serverSocket;
    private ExecutorService executorService;

    public StorageServerHandler (StorageServer storageServer, ServerSocket serverSocket, ExecutorService executorService) {
        this.storageServer = storageServer;
        this.executorService = executorService;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.socket = serverSocket.accept();
                this.executorService.execute(new ConnectedHandler(this.storageServer, this.socket));
                logger.log(Level.INFO, "CLIENT: connected " + socket.getRemoteSocketAddress());
            }
        } catch (IOException e) {
            logger.log(Level.INFO, "StorageServerHandler: IOException ");
            e.printStackTrace();
        }
    }
}
