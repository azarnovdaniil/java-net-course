package ru.daniilazarnov.util.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import ru.daniilazarnov.util.exception.IncorrectFileNameException;

public interface FileUtil {
  Path getPathByName(String name) throws IOException, IncorrectFileNameException;

  Set<Path> showFiles() throws IOException;

  void downToParent();
}
