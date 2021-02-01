package ru.uio.io;

import ru.uio.io.auth.AuthenticationService;


public interface Server {
    void broadcastMessage(String message);
    boolean isLoggedIn(String nickname);
    void subscribe(ClientHandler client);
    void unsubscribe(ClientHandler client);
    AuthenticationService getAuthenticationService();
    boolean whisper(String message, String toClientName);
}
