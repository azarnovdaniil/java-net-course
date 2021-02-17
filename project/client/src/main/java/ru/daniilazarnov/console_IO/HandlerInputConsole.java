package ru.daniilazarnov.console_IO;

import org.apache.log4j.Logger;
import ru.daniilazarnov.Command;
import ru.daniilazarnov.auth.AuthClient;
import ru.daniilazarnov.files_method.DeleteFile;
import ru.daniilazarnov.files_method.FileList;
import ru.daniilazarnov.network.NetworkCommunicationMethods;

import static ru.daniilazarnov.network.NetworkCommunicationMethods.*;


public class HandlerInputConsole {
    private static final Logger LOG = Logger.getLogger(HandlerInputConsole.class);
    private NetworkCommunicationMethods ncm = new NetworkCommunicationMethods();
    private AuthClient auth = new AuthClient();

    void commandConsoleHandler(String inputLine, Command command) {
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
            case UNKNOWN:
                System.out.println(Command.UNKNOWN);
                break;
            case SERVER:

                System.out.println(ncm.accessingTheServer(inputLine));
                break;
            case DELETE:
                System.out.println(
                        DeleteFile.deleteFile(inputLine));
                break;
            case RENAME:
                System.out.println(Command.RENAME);
                OutputConsole.printPrompt();
                break;
            default:
                LOG.error("Unexpected value: " + inputLine);
        }
    }
}
