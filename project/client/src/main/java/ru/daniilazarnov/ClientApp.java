package ru.daniilazarnov;

import java.io.IOException;
import java.util.Scanner;
import ru.daniilazarnov.util.exception.IncorrectCommandException;
import ru.daniilazarnov.util.exception.IncorrectPathException;

public class ClientApp {
  public static void main(String[] args) throws InterruptedException, IOException, IncorrectPathException, IncorrectCommandException {
    Client c = new Client("localhost", 9090);
    while (true) {
      try{
        Scanner in = new Scanner(System.in);
        String command = in.nextLine();
        c.commandPanel.doCommand(command);
      }catch (Exception e){
        e.printStackTrace();
      }
    }
  }

}
