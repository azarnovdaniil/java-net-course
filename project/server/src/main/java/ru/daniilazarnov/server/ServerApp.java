package ru.daniilazarnov.server;


import ru.daniilazarnov.server.auth.AuthService;
import ru.daniilazarnov.server.configuration.ServerConfiguration;
import ru.daniilazarnov.server.database.Authentication;
import ru.daniilazarnov.server.database.sql.ConnectionService;

public class ServerApp {


    public static void main(String[] args) {
        try {
            ServerConfiguration configuration = new ServerConfiguration();
            Authentication authDbService = new ru.daniilazarnov.server.database.sql.AuthService(
                    new ConnectionService(configuration.getDbUrl(),
                            configuration.getDbUser(),
                            configuration.getDbPassword()));
            Server ss = new Server(
                    configuration.getRoot(),
                    new AuthService(authDbService));
            ss.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
