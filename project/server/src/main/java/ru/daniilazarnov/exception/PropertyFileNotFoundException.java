package ru.daniilazarnov.exception;

public class PropertyFileNotFoundException extends Exception {
  public PropertyFileNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
