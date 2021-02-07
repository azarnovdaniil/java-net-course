package ru.daniilazarnov.util.command;

import java.io.IOException;
import java.util.List;
import ru.daniilazarnov.util.exception.IncorrectCommandException;
import ru.daniilazarnov.util.exception.IncorrectFileNameException;
import ru.daniilazarnov.util.exception.IncorrectPathException;

public interface CommandUtil {
  void doCommand(String command) throws IncorrectCommandException, IOException, IncorrectPathException, IncorrectFileNameException;

  void doCommand(List<String> commands) throws IncorrectCommandException, IOException, IncorrectPathException, IncorrectFileNameException;
}
