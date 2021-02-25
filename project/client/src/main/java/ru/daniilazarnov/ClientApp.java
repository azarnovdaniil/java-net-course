package ru.daniilazarnov;

import java.io.IOException;
import ru.daniilazarnov.util.exception.IncorrectFileNameException;

public class ClientApp {
  public static void main(String[] args) throws IOException, IncorrectFileNameException {
//    FileUtilImpl f = new FileUtilImpl();
//
//
//    ClientService c = new ClientService();
    int f = 345345;
    if(f > 255){
      f = 255;
    }
    System.out.println(f);
  }

}
