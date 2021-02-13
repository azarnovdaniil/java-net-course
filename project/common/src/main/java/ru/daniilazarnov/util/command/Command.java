package ru.daniilazarnov.util.command;

enum Command {
  /***
   * LS - is show all files and dir in current folder
   * CD + ... or dir - move to parent dir or another dir
   * MOVE - transfer file to server or client
   */
  LS("ls"), CD("cd"), MOVE("move"), BACK_TO_PARENT_FOLDER("...");

  private String command;

  Command(String command) {
    this.command = command;
  }

  public String getCommand() {
    return command;
  }
}
