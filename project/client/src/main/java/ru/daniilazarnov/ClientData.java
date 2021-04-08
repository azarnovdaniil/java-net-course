package ru.daniilazarnov;

import java.io.File;

public class ClientData {
    private static final int PORT = 8888;
    private static final String HOST = "localhost";
    private static final String STORAGE = "Project" + File.separator + "Client" + File.separator
            + "ClientStorage" + File.separator + "%s";

    public static int getPort() {
        return PORT;
    }

    public static String getHost() {
        return HOST;
    }
}
