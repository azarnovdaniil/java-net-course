package ru.daniilazarnov;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.daniilazarnov.ServerNetworkMethods.*;
import static ru.daniilazarnov.string_method.StringMethod.*;

/**
 * Содержит основную логику обработки введенных с консоли команд
 */
public class Client {
    private static final Logger LOG = Logger.getLogger(Client.class);
    private static final int DELAY = 10;
    private static final String PROMPT_TO_ENTER = ">";
    private static final String PROGRAM_NAME = "local_storage ";
    private static final String USERNAME = "~" + File.separator + "user1";
    private static final String HOME_FOLDER_PATH = Path.of("project", "client", "local_storage")
            .toString() + File.separator;


    public static void main(String[] args) throws IOException {
        init();
        auth();
        mainHandler();
    }

    /**
     * Метод содержит основную логику введенных с консоли команд
     */
    private static void mainHandler() throws IOException {
        String inputLine;
        while (true) {
            InputStream in = System.in;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            if (isConnect()) {
                while (true) {
                    if (!FileSender.isLoadingStatus()) {
                        printPrompt();
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
                            break;
                        case UPLOAD:
                            sendFile(inputLine);
                            break;
                        case LS:
                            System.out.println(getFilesList(inputLine));
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
                            printPrompt();
                            break;
                        default:
                            LOG.error("Unexpected value: " + inputLine);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Некорректная команда");
                    LOG.error(e);
                    mainHandler();
                }

            }
        }
    }

    private static void exit() {
        close();
        System.out.println("Bye");
        System.exit(0);
    }

    /**
     * Получает строку содержащую  файлы и каталоги по указанному пути
     *
     * @param inputLine введеная строка
     * @return результирующая строка имеет вид:  [папка] 'файл';
     */
    private static String getFilesList(String inputLine) {
        String result = "";
        String fileName;
        if (isThereaSecondElement(inputLine)) { // если после ls введено имя каталога получаем его
            fileName = getSecondElement(inputLine);
            if (!Files.isDirectory(Path.of(HOME_FOLDER_PATH + fileName))) {
                return "Файл не является каталогом";
            }
        } else {
            fileName = ""; // если имени каталога нет
        }
        try {
            result = UtilMethod.getFolderContents(fileName, "user");
        } catch (IOException e) {
            LOG.debug(e);
        }
        return result;
    }

    /**
     * Метод выводит на консоль строку приглашение ко вводу
     */
    protected static void printPrompt() {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print(PROGRAM_NAME + USERNAME + PROMPT_TO_ENTER);
    }
}

