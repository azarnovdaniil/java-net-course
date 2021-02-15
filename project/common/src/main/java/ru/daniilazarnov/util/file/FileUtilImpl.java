package ru.daniilazarnov.util.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ru.daniilazarnov.util.exception.IncorrectFileNameException;

public class FileUtilImpl implements FileUtil {
  private CurrentFolder folder;

  public FileUtilImpl(String path) {
    this.folder = new CurrentFolder(path);
  }

  public FileUtilImpl() {
    this.folder = new CurrentFolder();
  }

  public CurrentFolder getFolder() {
    return folder;
  }

  public void setFolder(Path path) {
    this.folder.setPath(path);
  }

  @Override
  public Set<Path> showFiles() throws IOException {
    Set<Path> result;
    try (Stream<Path> walk = Files.walk(folder.getPath(), 1)) {
      result = walk
        .collect(Collectors.toSet());
    }
    return result;
  }

  @Override
  public void downToParent() {
    folder.setPath(folder.getPath().getParent());
  }

  @Override
  public Path getPathByName(String name) throws IOException, IncorrectFileNameException {
    for (Path p : showFiles()) {
      if (name.equals(p.getFileName().toString())) {
        return p;
      }
    }
    throw new IncorrectFileNameException(name + " is not exist");
  }
}
