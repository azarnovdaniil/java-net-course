package ru.daniilazarnov.util.exception;

public class IncorrectCommandException extends Exception{
  public IncorrectCommandException(String errorMessage){
    super(errorMessage);
  }
}
