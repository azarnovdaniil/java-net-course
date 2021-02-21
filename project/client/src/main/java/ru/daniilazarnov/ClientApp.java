package ru.daniilazarnov;

import java.io.IOException;

public class ClientApp {
  public static void main(String[] args) throws IOException, InterruptedException {
    Client c = new Client();

    c.start();
  }

}
