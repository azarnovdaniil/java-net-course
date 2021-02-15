package ru.daniilazarnov.Exception;

public class PropertyFileNotFoundException extends Exception {
  public PropertyFileNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
