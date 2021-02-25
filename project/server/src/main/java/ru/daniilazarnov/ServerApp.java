package ru.daniilazarnov;

import java.io.IOException;
import ru.daniilazarnov.server.Server;

public class ServerApp {

  public static void main(String[] args) throws InterruptedException, IOException {
    Server app = new Server();
    app.start();
  }
}
