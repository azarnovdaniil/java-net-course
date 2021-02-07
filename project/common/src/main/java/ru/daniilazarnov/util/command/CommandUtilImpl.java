package ru.daniilazarnov.util.command;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import ru.daniilazarnov.util.exception.IncorrectCommandException;
import ru.daniilazarnov.util.exception.IncorrectFileNameException;
import ru.daniilazarnov.util.file.FileUtilImpl;

public class CommandUtilImpl implements CommandUtil {
  private FileUtilImpl fileUtil;

  public CommandUtilImpl() {
    this.fileUtil = new FileUtilImpl();
  }

  @Override
  public void doCommand(String command) throws IncorrectCommandException, IOException, IncorrectFileNameException {
    List<String> commands = Arrays.asList(command.split(" "));
    doCommand(commands);
  }

  @Override
  public void doCommand(List<String> commands) throws IncorrectCommandException, IOException, IncorrectFileNameException {
    if (commands.size() > 2 || commands.size() == 0) {
      throw new IncorrectCommandException("Incorrect command: " + commands);
    }
    if (commands.size() == 1 && Command.LS.getCommand().equals(commands.get(0))) {
      Set<Path> paths = fileUtil.showFiles();
      paths.forEach(p -> System.out.println(p.getFileName()));
      return;
    }
    if (Command.CD.getCommand().equals(commands.get(0))) {
      if (Command.BACK_TO_PARENT_FOLDER.getCommand().equals(commands.get(1))) {
        fileUtil.downToParent();
      } else {
        Path newPath = fileUtil.getPathByName(commands.get(1));
        fileUtil.setFolder(Paths.get(String.valueOf(newPath)));
      }
    }
  }
}
