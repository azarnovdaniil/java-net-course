package ru.daniilazarnov.server;


import ru.daniilazarnov.server.configuration.ServerConfiguration;

public class ServerApp {


    public static void main(String[] args) {
        try {

            Server ss = new Server(new ServerConfiguration());
            ss.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
