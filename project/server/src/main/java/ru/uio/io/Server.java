package ru.uio.io;

import ru.uio.io.auth.AuthenticationService;
import ru.uio.io.sql.JdbcSQLiteConnection;


public interface Server {
    void broadcastMessage(String message);
    boolean isLoggedIn(String nickname);
    void subscribe(ClientHandler client);
    void unsubscribe(ClientHandler client);
    AuthenticationService getAuthenticationService();
    JdbcSQLiteConnection getConnect();
}
