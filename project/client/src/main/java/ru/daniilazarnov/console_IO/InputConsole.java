package ru.daniilazarnov.console_IO;

import java.io.*;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import ru.daniilazarnov.FileSender;
import ru.daniilazarnov.ReceivingFiles;
import ru.daniilazarnov.enumeration.Command;
import ru.daniilazarnov.enumeration.State;
import ru.daniilazarnov.files_method.DeleteFile;
import ru.daniilazarnov.files_method.FileList;
import ru.daniilazarnov.network.Client;

import static ru.daniilazarnov.network.NetworkCommunicationMethods.*;
import static ru.daniilazarnov.string_method.StringMethod.*;
import static ru.daniilazarnov.string_method.StringMethod.getThirdElement;

/**
 * Содержит основную логику обработки введенных с консоли команд
 */
public class InputConsole {
    private static final Logger LOG = Logger.getLogger(InputConsole.class);
    private Client client;
    private final int timeout = 100;

    public void inputConsoleHandler() {
        client = new Client();

        String inputLine;
        InputStream in = System.in;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))) {
            //noinspection InfiniteLoopStatement
            while (true) {
                if (client.isConnect()) {
                    waitingForAnInvitation();
                    inputLine = bufferedReader.readLine().trim().toLowerCase();
                    String firstCommand = inputLine.split(" ")[0];
                    Command command = getCommand(firstCommand);

                    switch (command) {
                        case AUTH:
                            break;
                        case UPLOAD:
                            System.out.println(sendFile(inputLine, client));
                            break;
                        case LS:
                            System.out.println(FileList.getFilesList(inputLine));
                            break;
                        case EXIT:
                            exit(client);
                        case CONNECT:
                            System.out.println(connectedToServer(client));
                            break;
                        case DISCONNECT:
                            close(client);
                            break;
                        case DOWNLOAD:
                            sendNameFIleForDownloading(inputLine);
                            OutputConsole.printPrompt();
                            break;
                        case HELP:
                            System.out.println(Command.getHelpInfo());
                            break;
                        case STATUS:
                            System.out.println(getStatus(client));
                            break;
                        case UNKNOWN:
                            System.out.println(Command.UNKNOWN);
                            break;
                        case SERVER:
                            OutputConsole.setConsoleBusy(true);
                            System.out.println(accessingTheServer(inputLine));
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
                } else {
                    try {
                        TimeUnit.MILLISECONDS.sleep(timeout);
                    } catch (InterruptedException e) {
                        LOG.error(e);
                    }
                }
            }
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void sendNameFIleForDownloading(String inputLine) {
        if (isThereaSecondElement(inputLine)) {
            String command = getSecondElement(inputLine);
            client.sendStringAndCommand(command, Command.UPLOAD.getCommandByte());
        } else {
            System.out.println("local_storage: некорректный управляющий байт");
        }
    }

    void waitingForAnInvitation() {
        do {
            if (!FileSender.isLoadingStatus()  // удалить
                    && ReceivingFiles.getCurrentState() == State.IDLE
                    && !OutputConsole.isConsoleBusy()) {
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

    public String accessingTheServer(String inputLine) {
        String result;
        if (isThereaThirdElement(inputLine)) { // если после ls введено имя каталога получаем его
            String folderName = getThirdElement(inputLine);
            Command command = Command.valueOf(getThirdElement(inputLine).toUpperCase());
            if (command == Command.LS) {
                OutputConsole.setConsoleBusy(true);
                client.sendStringAndCommand(folderName, Command.LS.getCommandByte());
            } else {
                return "Неизвестная команда";
            }
        } else {
            client.sendStringAndCommand("", Command.LS.getCommandByte());
        }
        result = "local_storage: Запрос отправлен на сервер";
        return result;
    }

}
