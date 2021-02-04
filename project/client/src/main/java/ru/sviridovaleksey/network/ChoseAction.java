package ru.sviridovaleksey.network;

import ru.sviridovaleksey.Command;

import java.util.Scanner;

public class ChoseAction {

    private Connection connection;
    private Scanner scanner;

    public ChoseAction(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void choseAction (String chose) {

        Command command;
        String userName = "user1";

        switch (chose) {

            case "0" : {
                break;
            }

            case "1" : {
                System.out.println("Выбрано действие создать директорию, введите название директории:");
                String nameDirectory = scanner.next();
                if (nameDirectory.equals("0")) { break;}
                command = Command.createNewDirectory(userName, nameDirectory);
                connection.writeMessage(command);
                break;
            }

            case "2" : {
                System.out.println("Выбрано действие удалить директорию, введите номер директории:");
                String numberDirectory = scanner.next();
                if (numberDirectory.equals("0")) { break;}
                command = Command.deleteDirectory(userName, numberDirectory);
                connection.writeMessage(command);
                break;
            }

            case "3" : {
                System.out.println("Выбрано действие создать файл, введите имя файла:");
                String fileName = scanner.next();
                if (fileName.equals("0")) { break;}
                command = Command.createNewFile(userName, fileName);
                connection.writeMessage(command);
                break;
            }

            case "4" : {
                System.out.println("Выбрано действие удалить файл, введите номер файла:");
                String numberFile = scanner.next();
                if (numberFile.equals("0")) { break;}
                command = Command.deleteFile(userName, numberFile);
                connection.writeMessage(command);
                break;

            }

            case "5" : {
                System.out.println("Выбрано действие копировать файл, введите номер файла:");
                String numberFile = scanner.next();
                if (numberFile.equals("0")) { break;}
                command = Command.copyFile(userName, numberFile);
                connection.writeMessage(command);
                break;
            }

            case "6" : {
                System.out.println("Выбрано действие вставить файл, введите paste для вставки:");
                command = Command.pasteFile(userName);
                connection.writeMessage(command);
                break;
            }

            case  "7" : {
                System.out.println("Выбрано действие отправить сообщение на сервер, введите сообщение");
                String message = scanner.next();
                if (message.equals("0")) { break;}
                command = Command.message(userName, message);
                connection.writeMessage(command);
            }


        }

    }

}
