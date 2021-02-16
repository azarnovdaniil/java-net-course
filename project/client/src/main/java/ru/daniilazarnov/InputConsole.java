package ru.daniilazarnov;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.daniilazarnov.NetworkCommunicationMethods.*;
import static ru.daniilazarnov.string_method.StringMethod.*;

/**
 * Содержит основную логику обработки введенных с консоли команд
 */
public class InputConsole {
    private static final Logger LOG = Logger.getLogger(InputConsole.class);
    private static final int DELAY = 10;
    private static final String PROMPT_TO_ENTER = ">";
    private static final String PROGRAM_NAME = "local_storage ";
    private static final String USERNAME = "~" + File.separator + "user1";
    private static final String WELCOME_MESSAGE = "Добро пожаловать в файловое хранилище!\n"
            + "ver: 0.002a\n"
            + "uod: 09.02.2021\n";
    private static final String HOME_FOLDER_PATH = Path.of("project", "client", "local_storage")
            .toString() + File.separator;


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
                        case DELETE:
                            System.out.println(Command.DELETE);
                            System.out.println(deleteFile(inputLine));
                            printPrompt();
                            break;
                        case RENAME:
                            System.out.println(Command.RENAME);
                            printPrompt();
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

    private static String deleteFile(String inputLine) {
        String result = "";
        String fileName;
        if (isThereaSecondElement(inputLine)) { // если после ls введено имя каталога получаем его
            fileName = getSecondElement(inputLine);
            Path path = Paths.get(HOME_FOLDER_PATH, fileName);
            if (Files.exists(path)) {
                UtilMethod.deleteFile(path.toString());
                if (Files.exists(path)) {
                    return "Не удалось удалить указанный файл";
                } else {
                    return "Файл успешно удален";
                }
            } else {
                return "Неправильное имя файла";
            }
        }


        return result;


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

