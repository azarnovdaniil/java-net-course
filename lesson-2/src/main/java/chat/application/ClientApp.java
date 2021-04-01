package chat.application;


import chat.client.ClientChatAdapter;

public class ClientApp {


    public static final int PORT = 8888;
    public static final String HOST = "localhost";

    public static void main(String[] args) {
        new ClientChatAdapter(HOST, PORT);
    }
}
