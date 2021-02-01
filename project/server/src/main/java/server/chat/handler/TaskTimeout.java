package server.chat.handler;

import java.util.TimerTask;

public class TaskTimeout extends TimerTask {
    private ClientHandler handler;

    public TaskTimeout(ClientHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        if (!handler.isAction()) {
            handler.closeConnection(false);
            System.out.println("Долгое бездействие, соединение разорвано");
        }
    }
}
