package ru.daniilazarnov;
/**
 * Класс для обработки команд пользователя
 */

import ru.daniilazarnov.commands.*;
import java.util.Scanner;

public class CommandInputLevel {
    private static boolean next=true;

    public static void invoke(MessagePacket messagePacket) {
        PrintMessages.invoke("MessageAboutCommands.txt");
        Scanner scanner = new Scanner(System.in);

        while (next) {
            System.out.println("Пожалуйста, введите вашу команду: ");
            String command = scanner.nextLine().toUpperCase().trim();
            switch (command) {
                case "*UPLOAD":
                    messagePacket.setCommand(new UploadFile());
                    System.out.println("Пожалуйста, укажите имя файла для загрузки на сервер: ");
                    String fileName = scanner.nextLine().toLowerCase().trim();
                    messagePacket.setFileName(fileName);
                    next = false;
                    break;
                case "*DOWNLOAD":
                    messagePacket.setCommand(new DownloadFile());
                    break;
                case "*SHOW":
                    messagePacket.setCommand(new ShowFile());
                    next = false;
                    break;
                case "*DELETE":
                    messagePacket.setCommand(new DeleteFile());
                    break;
                case "*RENAME":
                    messagePacket.setCommand(new RenameFile());
                    break;
                case "*HELP":
                    PrintMessages.invoke("MessageAboutCommands.txt");
                    break;
                case "*HELLO":
                    PrintMessages.invoke("MessageHello.txt");
                    break;
                case "*EXIT":
                    messagePacket.setCommand(new Exit());
                    next = false;
                    scanner.close();
                    break;
                default:
                    System.err.print("Ошибка");
                    System.out.print(": команда \"" + command + "\" не распознана.\n");
            }

        }
    }
}
