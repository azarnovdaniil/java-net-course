package ru.daniilazarnov.console_IO;

import org.apache.log4j.Logger;
import ru.daniilazarnov.enumeration.Command;
import ru.daniilazarnov.FileSender;
import ru.daniilazarnov.ReceivingFiles;
import ru.daniilazarnov.enumeration.State;

public class ConsoleMethod {
    private static final Logger LOG = Logger.getLogger(ConsoleMethod.class);

    void waitingForAnInvitation() {
        do {
            if (consoleIsNotBusy()) {
                OutputConsole.printPrompt();
                break;
            }
        } while (true);
    }

    Command getCommand(String firstCommand) {
        Command command = Command.UNKNOWN;
        try {
            command = Command.valueOf(firstCommand.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            LOG.error(e);
        }
        return command;
    }

    boolean consoleIsNotBusy() {
        return
                !FileSender.isLoadingStatus()  // удалить
                        && ReceivingFiles.getCurrentState() == State.IDLE
                        && !OutputConsole.isConsoleBusy();
    }
}
