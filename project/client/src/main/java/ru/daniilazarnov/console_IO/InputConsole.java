package ru.daniilazarnov.console_IO;

import org.apache.log4j.Logger;
import ru.daniilazarnov.*;
import ru.daniilazarnov.files_method.DeleteFile;
import ru.daniilazarnov.files_method.FileList;

import java.io.*;

import static ru.daniilazarnov.NetworkCommunicationMethods.*;

/**
 * Содержит основную логику обработки введенных с консоли команд
 */
public class InputConsole {
    private static final Logger LOG = Logger.getLogger(InputConsole.class);

    private static final String WELCOME_MESSAGE = "Добро пожаловать в файловое хранилище!\n"
            + "ver: 0.002a\n"
            + "uod: 09.02.2021\n";

    public static void main(String[] args) throws IOException {
        init();
        while (!ClientNetworkHandler.isAuth()) {

            auth();
            if (ClientNetworkHandler.isAuth()) {
                init();
            } else {
                break;
            }
        }
        System.out.print(WELCOME_MESSAGE);
        inputConsoleHandler();
    }

    /**
     * Метод содержит основную логику введенных с консоли команд
     */
    private static void inputConsoleHandler() throws IOException {
        String inputLine;
        while (true) {
            InputStream in = System.in;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            if (isConnect()) {
                while (true) {
                    if (!FileSender.isLoadingStatus()) {
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
                            System.out.println(Auth.getStatusAuth());
                            break;
                        case UPLOAD:
                            sendFile(inputLine);
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
                            sendNameFIleForDownloading(inputLine);
                            break;
                        case HELP:
                            System.out.println(Command.getHelpInfo());
                            break;
                        case STATUS:
                            System.out.println(getStatus());
                            break;
                        case SERVER:
                            System.out.println(accessingTheServer(inputLine));
                            OutputConsole.printPrompt();
                            break;
                        case DELETE:
                            System.out.println(Command.DELETE);
                            System.out.println(
                                    DeleteFile.deleteFile(inputLine));
                            OutputConsole.printPrompt();
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




    private static void exit() {
        close();
        System.out.println("Bye");
        System.exit(0);
    }




}

