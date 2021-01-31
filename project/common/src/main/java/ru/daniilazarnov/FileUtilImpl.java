package ru.daniilazarnov;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class FileUtilImpl implements FileUtil {
  private CurrentFolder folder;

  public FileUtilImpl() {
    this.folder = new CurrentFolder();
  }

  @Override
  public Set<String> showFiles() throws IOException {
    DirectoryStream<Path> stream = Files.newDirectoryStream(folder.getPath());
    Set<String> result = new HashSet<>();
    stream.forEach(f -> {
      result.add(f.getFileName().toString());
    });

    return result;
  }

  public void downToParent() {
    folder.setPath(folder.getPath().getParent());
  }

  @Override
  public Set<File> getFiles() throws IOException {
    Stream<Path> stream = Files.walk(folder.getPath());

    return null;
  }

  @Override
  public File getFileByName(String name) {
    return null;
  }
}
