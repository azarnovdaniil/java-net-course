package server;

import auth.AuthenticationService;

import java.util.Set;

public interface Server {
    void broadcastMessage(String message);
    void sendPrivateMessage(String from, String name, String privateMessage);
    boolean isLoggedIn(String nickname);
    void subscribe(ClientHandler client);
    void unsubscribe(ClientHandler client);
    AuthenticationService getAuthenticationService();
    Set<ClientHandler> getClients();
    boolean changeNickname(String oldNickName, String newNickName, String password);
}
