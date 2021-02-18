package ru.daniilazarnov.console_IO;

import java.io.*;

import org.apache.log4j.Logger;
import ru.daniilazarnov.enumeration.Command;

import static ru.daniilazarnov.network.NetworkCommunicationMethods.*;

/**
 * Содержит основную логику обработки введенных с консоли команд
 */
public class InputConsole {
    private static final Logger LOG = Logger.getLogger(InputConsole.class);
    private final HandlerInputConsole handler = new HandlerInputConsole();
    private final ConsoleMethod cm = new ConsoleMethod();


    /**
     * Метод содержит основную логику введенных с консоли команд
     */
    public void inputConsoleHandler() throws IOException {
        String inputLine;
        InputStream in = System.in;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))) {
            //noinspection InfiniteLoopStatement
            while (true) {
                if (isConnect()) {
                    LOG.debug("isConnect(): " + isConnect());
                    cm.waitingForAnInvitation();
                    inputLine = bufferedReader.readLine().trim().toLowerCase();
                    String firstCommand = inputLine.split(" ")[0];
                    Command command = cm.getCommand(firstCommand);
                    handler.commandConsoleHandler(inputLine, command);
                }

            }
        }
    }
}
