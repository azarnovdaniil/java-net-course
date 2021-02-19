package ru.daniilazarnov.network;

import org.apache.log4j.Logger;
import ru.daniilazarnov.console_IO.OutputConsole;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.daniilazarnov.string_method.StringMethod.*;
import static ru.daniilazarnov.constants.Constants.*;
import static ru.daniilazarnov.string_method.StringMethod.getSecondElement;

public class NetworkCommunicationMethods {
    private static final Logger LOG = Logger.getLogger(NetworkCommunicationMethods.class);

        public static void init() {
            Client client = new Client();
        }

        public static String connectedToServer(Client client) {
            if (!client.isConnect()) {

                init();
                return "Соединение с сервером получено";
            } else {
                return "Соединение с сервером уже активно";
            }
        }

        public static String getStatus(Client client) {
            if (client.isConnect()) {
                return "Сервер доступен";
            } else {
                return "соединение с сервером отсутствует";
            }
        }

        public static String sendFile(String inputLine, Client client) {
            String result = "local_storage: ";
            if (isThereaSecondElement(inputLine)) {
                String fileName = getSecondElement(inputLine);

                if (isFileExists(fileName)) { // проверяем, существует ли файл
                    OutputConsole.setConsoleBusy(true);
                    client.sendFile(DEFAULT_PATH_USER + File.separator + fileName); // Отправка файла
                    result += "Файл отправлен";

                } else {
                    result += "Файл не найден";
                }
            } else {
                System.out.println("local_storage: некорректный аргумент");
                result += " некорректный аргумент";
            }
            return result;
        }

        public static void exit(Client client) {
            close(client);
            System.out.println("Bye");
            System.exit(0);
        }

        private static boolean isFileExists(String fileName) {
            return Files.exists(Path.of(DEFAULT_PATH_USER, fileName));
        }

        public static void close(Client client) {
            client.close();
        }
    }
