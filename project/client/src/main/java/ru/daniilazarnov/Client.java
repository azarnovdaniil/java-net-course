package ru.daniilazarnov;

import ru.daniilazarnov.util.command.CommandUtilImpl;

public class Client {
  private final String host;
  private final int port;
  public CommandUtilImpl commandPanel;

  public Client(String host, int port) {
    this.host = host;
    this.port = port;
    this.commandPanel = new CommandUtilImpl();
  }

}
