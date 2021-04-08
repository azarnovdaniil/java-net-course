package ru.daniilazarnov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class ClientPathHolder implements PathHolder {

    String pathToRepo;
    long fileLength;
    Consumer<String> print;
    private final Consumer<String> systemMessage;
    private static final Logger LOGGER = LogManager.getLogger(ClientPathHolder.class);

    /**
     * Tool and storage class to provide channel handlers with general information and tools for triggering App
     * facilities.
     *
     * @param pathToRepo    - path to local file storage.
     * @param print         - transfers server-involved commands result to the console.
     * @param systemMessage - inits a respond to the server when reply-needed actions happens.
     */

    ClientPathHolder(String pathToRepo, Consumer<String> print, Consumer<String> systemMessage) {
        this.pathToRepo = pathToRepo;
        this.print = print;
        this.systemMessage = systemMessage;
    }

    /**
     * Informs the console that channel is not active and server will give no respond.
     */
    public void channelNotActive() {
        this.print.accept("No connection with server applied...");
    }

    /**
     * Informs both server and user than file downloaded successfully.
     */
    @Override
    public void transComplete() {
        this.print.accept("File downloaded successfully!");
        systemMessage.accept("RESPOND");
        LOGGER.info("File downloaded successfully.");
    }

    @Override
    public void setAuthority(String path) {
        this.pathToRepo = path;
    }

    @Override
    public String getAuthority() {
        return this.pathToRepo;
    }

    @Override
    public void setFileLength(long length) {
        this.fileLength = length;
    }

    @Override
    public long getFileLength() {
        return this.fileLength;
    }

    @Override
    public void sendMessage(String message) {
        System.out.println("Empty method");
    }

    @Override
    public String getLogin() {
        return null;
    }
}
