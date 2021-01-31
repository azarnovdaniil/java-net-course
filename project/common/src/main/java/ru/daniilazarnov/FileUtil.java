package ru.daniilazarnov;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public interface FileUtil {
  Set<File> getFiles() throws IOException;

  File getFileByName(String name);

  Set<String> showFiles() throws IOException;
}
