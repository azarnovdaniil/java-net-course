package ru.daniilazarnov;

import java.nio.file.Path;

public class CurrentFolder {
  private Path path;

  public CurrentFolder() {
    this.path = Path.of(System.getProperty("user.dir"));
  }

  public CurrentFolder(Path path) {
    this.path = path;
  }

  public Path getPath() {
    return path;
  }

  public void setPath(Path path) {
    this.path = path;
  }

}
