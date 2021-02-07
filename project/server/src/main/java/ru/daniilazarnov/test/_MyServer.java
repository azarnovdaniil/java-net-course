package ru.daniilazarnov.test;

import ru.daniilazarnov.Server;
import ru.daniilazarnov.test._ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class _MyServer {
    private ExecutorService executorService;
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private ServerSocket serverSocket;
    private Socket socket;
    private final int PORT = 8189;


    public ExecutorService getExecutorService() {
        return executorService;
    }

    public _MyServer() {
        executorService = Executors.newCachedThreadPool();

        try {
            serverSocket = new ServerSocket(PORT);
            logger.info("server started!");
            while (true) {
                socket = serverSocket.accept();
                //System.out.println("client connected " + socket.getRemoteSocketAddress());
                logger.info("client connected " + socket.getRemoteSocketAddress());
                new _ClientHandler(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logger.info("server closed");
            executorService.shutdown();
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
}
