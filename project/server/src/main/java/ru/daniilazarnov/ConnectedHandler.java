package ru.daniilazarnov;

import ru.daniilazarnov.data.CommonData;
import ru.daniilazarnov.data.TypeMessages;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectedHandler implements Runnable {

    private static final Logger logger = Logger.getLogger(ConnectedHandler.class.getName());

    private boolean isAuthorized = false;
    private StorageServer storageServer;
    private Socket socket;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;


    public ConnectedHandler (StorageServer storageServer, Socket socket) {
        this.storageServer = storageServer;
        this.socket = socket;

        try {
            this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            logger.log(Level.WARNING, "ConnectedHandler: IOException");
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                CommonData data = (CommonData) this.objectInputStream.readObject();

                if (!this.isAuthorized && data.getType() != TypeMessages.AUTH) {
                    continue;
                }

                System.out.println(data);
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.WARNING, "ConnectedHandler runnable catch IOException");
        } finally {
            try {
                socket.close();
            } catch (IOException ioException) {
                logger.log(Level.WARNING, "ConnectedHandler Runnable catch socket.close() ");
            }
        }
    }
}
