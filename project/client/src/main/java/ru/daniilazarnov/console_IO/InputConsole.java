package ru.daniilazarnov.console_IO;

import java.io.*;

import org.apache.log4j.Logger;
import ru.daniilazarnov.*;
import ru.daniilazarnov.auth.AuthClient;
import ru.daniilazarnov.files_method.DeleteFile;
import ru.daniilazarnov.files_method.FileList;
import ru.daniilazarnov.network.NetworkCommunicationMethods;

import static ru.daniilazarnov.network.NetworkCommunicationMethods.*;

/**
 * Содержит основную логику обработки введенных с консоли команд
 */
public class InputConsole {
    private static final Logger LOG = Logger.getLogger(InputConsole.class);
    private NetworkCommunicationMethods ncm = new NetworkCommunicationMethods();
    private AuthClient auth = new AuthClient();

    /**
     * Метод содержит основную логику введенных с консоли команд
     */
    public void inputConsoleHandler() throws IOException {
        String inputLine;
        while (true) {
            InputStream in = System.in;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            if (isConnect()) {
                while (true) {
                    if (!FileSender.isLoadingStatus() && ReceivingFiles.getCurrentState() == State.IDLE) {
                        OutputConsole.printPrompt();
                        break;
                    }
                }
                inputLine = bufferedReader.readLine().trim().toLowerCase();
                String firstCommand = inputLine.split(" ")[0];
                Command command;
                try {
                    command = Command.valueOf(firstCommand.toUpperCase());

                    switch (command) {
                        case AUTH:
                            System.out.println(auth.getStringStatusAuth());
                            break;
                        case UPLOAD:
                            System.out.println(sendFile(inputLine));
                            break;
                        case LS:
                            System.out.println(FileList.getFilesList(inputLine));
                            break;
                        case EXIT:
                            exit();
                        case CONNECT:
                            System.out.println(connectedToServer());
                            break;
                        case DISCONNECT:
                            close();
                            break;
                        case DOWNLOAD:
                            ncm.sendNameFIleForDownloading(inputLine);
                            OutputConsole.printPrompt();
                            break;
                        case HELP:
                            System.out.println(Command.getHelpInfo());
                            break;
                        case STATUS:
                            System.out.println(getStatus());
                            break;
                        case SERVER:
                            System.out.println(ncm.accessingTheServer(inputLine));
//                            OutputConsole.printPrompt();
                            break;
                        case DELETE:
                            System.out.println(
                                    DeleteFile.deleteFile(inputLine));
//                            OutputConsole.printPrompt();
                            break;
                        case RENAME:
                            System.out.println(Command.RENAME);
                            OutputConsole.printPrompt();
                            break;
                        default:
                            LOG.error("Unexpected value: " + inputLine);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Некорректная команда");
                    LOG.error(e);
                    inputConsoleHandler();
                }

            }
        }
    }
}
