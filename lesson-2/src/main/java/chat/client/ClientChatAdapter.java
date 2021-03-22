package chat.client;

import chat.gui.ChatFrame;
import chat.helpers.FileHelper;

public class ClientChatAdapter {
    private ChatFrame chatFrame;
    private Client client;

    public ClientChatAdapter(String host, int port) {
        client = new Client(host, port);
        chatFrame = new ChatFrame(messageFromFormSubmitListener -> client.sendMessage(messageFromFormSubmitListener));
        //2
        chatFrame.append(FileHelper.readHistory(100));
        read();
    }

    private void read() {
        new Thread(() -> {
            try {

                while (true) {
                    String message = client.receiveMessage();
                    chatFrame.append(message);
                    //1
                    FileHelper.writeHistory(message);
                }
            } catch (ClientConnectionException e) {
                e.printStackTrace();
            } finally {
                if (client != null) {
                    client.close();
                }
            }
        }).start();
    }
}
