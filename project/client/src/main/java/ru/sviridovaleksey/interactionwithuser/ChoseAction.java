package ru.sviridovaleksey.interactionwithuser;

import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.workwithfile.ShowAllDirectoryClient;
import ru.sviridovaleksey.workwithfile.WorkWithFileClient;
import java.io.File;
import java.util.Scanner;

public class ChoseAction {

    private final String separator = "\\";
    private final Scanner scanner = new Scanner(System.in);
    private final Channel channel;
    private final WorkWithFileClient workWithFileClient;
    private final ShowAllDirectoryClient showAllDirectoryClient = new ShowAllDirectoryClient();
    private String whereClient;


    public ChoseAction(Channel channel, WorkWithFileClient workWithFileClient, String whereClient) {
        this.whereClient = whereClient;
        this.channel = channel;
        this.workWithFileClient = workWithFileClient;
    }
    public void choseAction(String chose, String userName) {
        Command command;
        if (chose.equals("0")) {
            return;
        } else if (chose.equals("a")) {
            command = Command.getShowDir(userName, "");
            channel.writeAndFlush(command);

        } else if (chose.equals("1")) {
            System.out.println("Выбрано действие создать директорию, введите название директории:");
            String nameDirectory = scanner.next();
            if (nameDirectory.equals("0")) {
                return;
            }
            command = Command.createNewDirectory(userName, nameDirectory);
            channel.writeAndFlush(command);

        } else if (chose.equals("2")) {
            System.out.println("Выбрано действие удалить директорию, введите имя директории:");
            String numberDirectory = scanner.next();
            if (numberDirectory.equals("0")) {
                return;
            }
            command = Command.deleteDirectory(userName, numberDirectory);
            channel.writeAndFlush(command);
        } else if (chose.equals("3")) {
            System.out.println("Выбрано действие создать файл, введите имя файла:");
            String fileName = scanner.next();
            if (fileName.equals("0")) {
                return;
            }
            command = Command.createNewFile(userName, fileName);
            channel.writeAndFlush(command);
        } else if (chose.equals("4")) {
            System.out.println("Выбрано действие удалить файл, введите имя файла:");
            String nameFile = scanner.next();
            if (nameFile.equals("0")) {
                return;
            }
            command = Command.deleteFile(userName, nameFile);
            channel.writeAndFlush(command);

        } else if (chose.equals("5")) {
            System.out.println("Выбрано действие переименовать файл, введите название файла,"
                    + " которое хотите переименовать:");
            String nameOldFile = scanner.next();
            if (nameOldFile.equals("0")) {
                return;
            }
            System.out.println("Введите новое имя");
            String nameNewFile = scanner.next();
            if (nameNewFile.equals("0")) {
                return;
            }
            command = Command.renameFile(userName, nameOldFile, nameNewFile);
            channel.writeAndFlush(command);
        } else if (chose.equals("6")) {
            System.out.println("Выбрано действие отправить сообщение на сервер, введите сообщение");
            String message = scanner.next();
            if (message.equals("0")) {
                return;
            }
            command = Command.message(userName, message);
            channel.writeAndFlush(command);
        } else if (chose.equals("7")) {
            System.out.println("Выбрано действие открыть директорию, введите название директории");
            String message = scanner.next();
            if (message.equals("0")) {
                return;
            }
            command = Command.getShowDir(userName, message);
            channel.writeAndFlush(command);

        } else if (chose.equals("8")) {
            System.out.println("Выбрано действие 'назад'");
            command = Command.getBackDir(userName, "");
            channel.writeAndFlush(command);

        } else if (chose.equals("9")) {
            System.out.println("Выбрано действие отправить файл на серер");
            System.out.println("Введите путь к файлу (пример D:/test.txt) :    (0 - назад)");
            String way = scanner.next();
            if (way.equals("0")) {
                return;
            }
            File file = new File(way);
            command = Command.createNewFile(userName, file.getName());
            channel.writeAndFlush(command);
            String ansirest = "\u001B[0m";
            String ansired = "\u001B[31m";
            System.out.println(ansired + "Дождитесь ответа от сервера о завершении" + ansirest);
            workWithFileClient.sendFileInServer(file, userName, channel);
        } else if (chose.equals("10")) {
            System.out.println("Выбрано действие скачать файл, введине название файла     (0 - назад)");
            String fileName = scanner.next();
            if (fileName.equals("0")) {
                return;
            }
            command = Command.requestFile(userName, fileName);
            channel.writeAndFlush(command);

        } else if (chose.equals("11")) {
            System.out.println("Выбрано действие 'показать локальную директорию, введите адрес название");
            String nameFile = scanner.next();
            if (nameFile.equals("0")) {
                return;
            }
            whereClient = whereClient + separator + nameFile;
            System.out.println(showAllDirectoryClient.startShowDirectory(whereClient));

        } else if (chose.equals("12")) {
            System.out.println("Выбрано действие 'удалить локальную директорию или файл, введите адрес название");
            String way = whereClient + separator + scanner.next();
            if (way.equals("0")) {
                return;
            }
            String getNewLink = getLink(way);
            workWithFileClient.deleteFile(way);
            System.out.println(getNewLink + separator);
            whereClient  = getNewLink;
            System.out.println(showAllDirectoryClient.startShowDirectory(getNewLink));
        } else if (chose.equals("13")) {
            System.out.println("Выбрано действие 'переименовать файл на локальном устр., введите название");
            String oldName = whereClient + separator + scanner.next();
            if (oldName.equals("0")) {
                return;
            }
            String getNewLink = getLink(oldName) + separator;
            System.out.println("Введите новое имя файла:");
            String newName = scanner.next();
            if (newName.equals("0")) {
                return;
            }
            workWithFileClient.renameFile(oldName, getNewLink + newName);
            whereClient = getNewLink;
            System.out.println(showAllDirectoryClient.startShowDirectory(getNewLink));
        } else if (chose.equals("end")) {
            System.out.println("Выбрано действие закрыть приложение, пока ");
            System.exit(0);
        } else if (chose.equals("b")) {
            System.out.println(whereClient);
            System.out.println(showAllDirectoryClient.startShowDirectory(whereClient));
        }
    }

    private String getLink(String link) {
        String getNewLink = StringUtils.reverse(link);
        getNewLink = StringUtils.substringAfter(getNewLink, separator);
        getNewLink = StringUtils.reverse(getNewLink);
        return getNewLink;
    }
}
