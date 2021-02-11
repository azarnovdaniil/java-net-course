package ru.daniilazarnov;

import java.io.IOException;
import java.util.logging.*;

public class StartServer {
    private static final Logger logger = Logger.getLogger ("");

    public static void main(String[] args) {
        try {
            Handler fileHandler = new FileHandler ("log_%g.txt",20 * 1025,3,true);
            fileHandler.setFormatter (new SimpleFormatter ());
            logger.addHandler (fileHandler);

        } catch (IOException e) {
            logger.log (Level.SEVERE, "Файл логирования не найден", e);
            e.printStackTrace ();
        }

        new Server();
    }
}
