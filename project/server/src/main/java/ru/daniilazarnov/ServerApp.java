package ru.daniilazarnov;

public class ServerApp {
  public static void main(String[] args) throws InterruptedException {
    Server app = new Server();
    app.start(8989);
  }
}
