import com.chat.auth.AuthenticationService;

public interface Server {
    void broadcastMsg(String msg); //принимать сообщение от одного клиента и донести до других
    boolean isLoggedIn (String nickname);
    void subscribe(ClientHandler client);
    void unsubscribe(ClientHandler client);
    AuthenticationService getAuthenticationService(); // Исключение, т.к. делаем чатсью интерф
}


