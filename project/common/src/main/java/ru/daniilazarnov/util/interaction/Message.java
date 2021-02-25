package ru.daniilazarnov.util.interaction;

import java.io.File;
import java.io.Serializable;

public class Message implements Serializable {
  private String command;
  private File file;
  private String fileName;

  public Message() {

  }

  public Message(String command) {
    this.command = command;
  }

  public Message(String command, File file) {
    this.command = command;
    this.file = file;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  @Override
  public String toString() {
    return "Message{" +
      "command='" + command + '\'' +
      '}';
  }
}
