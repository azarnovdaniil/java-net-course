package ru.daniilazarnov;
import java.util.logging.Logger;

public class StartClient {
    private static final Logger logger = Logger.getLogger(StartClient.class.getName());

    public static void main(String[] args) {
        Network network = new Network();
        if (!network.connect()) {
            logger.info("Ошибка подключения к серверу");
        }
        network.waitMessage();
    }
}
