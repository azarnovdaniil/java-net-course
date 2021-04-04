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


    ClientPathHolder(String pathToRepo, Consumer<String> print, Consumer<String> systemMessage) {
        this.pathToRepo = pathToRepo;
        this.print = print;
        this.systemMessage = systemMessage;
    }

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
