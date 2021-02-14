package ru.sviridovaleksey.interactionwithuser;

import io.netty.channel.Channel;
import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.workwithfile.WorkWithFileClient;
import java.io.File;
import java.util.Scanner;

public class ChoseAction {

    private Scanner scanner = new Scanner(System.in);
    private final Channel channel;
    private final WorkWithFileClient workWithFileClient;
    private  final String ANSI_RESET = "\u001B[0m";
    private  final String ANSI_RED = "\u001B[31m";



    public ChoseAction(Channel channel, WorkWithFileClient workWithFileClient) {
        this.channel = channel;
        this.workWithFileClient = workWithFileClient;
    }

    public void choseAction (String chose, String userName) {

        Command command;

        switch (chose) {

            case "0" : {
                break;
            }

            case "a" : {
                command = Command.getShowDir(userName, "");
                channel.write(command);
                break;
            }

            case "1" : {
                System.out.println("Выбрано действие создать директорию, введите название директории:");
                String nameDirectory = scanner.next();
                if (nameDirectory.equals("0")) { break;}
                command = Command.createNewDirectory(userName, nameDirectory);
                channel.write(command);
                break;
            }

            case "2" : {
                System.out.println("Выбрано действие удалить директорию, введите имя директории:");
                String numberDirectory = scanner.next();
                if (numberDirectory.equals("0")) { break;}
                command = Command.deleteDirectory(userName, numberDirectory);
                channel.write(command);
                break;
            }

            case "3" : {
                System.out.println("Выбрано действие создать файл, введите имя файла:");
                String fileName = scanner.next();
                if (fileName.equals("0")) { break;}
                command = Command.createNewFile(userName, fileName);
                channel.write(command);
                break;
            }

            case "4" : {
                System.out.println("Выбрано действие удалить файл, введите имя файла:");
                String nameFile = scanner.next();
                if (nameFile.equals("0")) { break;}
                command = Command.deleteFile(userName, nameFile);
                channel.write(command);
                break;

            }

            case "5" : {
                System.out.println("Выбрано действие копировать файл, введите имя файла:");
                String nameFile = scanner.next();
                if (nameFile.equals("0")) { break;}
                command = Command.copyFile(userName, nameFile);
                channel.write(command);
                break;
            }

            case "6" : {
                System.out.println("Выбрано действие вставить файл, введите paste для вставки:");
                command = Command.pasteFile(userName);
                channel.write(command);
                break;
            }

            case  "7" : {
                System.out.println("Выбрано действие отправить сообщение на сервер, введите сообщение");
                String message = scanner.next();
                if (message.equals("0")) { break;}
                command = Command.message(userName, message);
                channel.write(command);
                break;
            }

            case  "8" : {
                System.out.println("Выбрано действие открыть директорию, введите название директории");
                String message = scanner.next();
                if (message.equals("0")) { break;}
                command = Command.getShowDir(userName, message);
                channel.write(command);
                break;
            }

            case "9" : {
                System.out.println("Выбрано действие 'назад'");
                command = Command.getBackDir(userName,"");
                channel.write(command);
                break;
            }

            case "10" : {
                System.out.println("Выбрано действие отправить файл на серер");
                System.out.println("Введите путь к файлу (пример D:/test.txt) :    (0 - назад)");
                String way = scanner.next();
                if (way.equals("0")) { break;}
                File file = new File(way);
                command = Command.createNewFile(userName, file.getName());
                channel.write(command);
                System.out.println(ANSI_RED + "Дождитесь ответа от сервера о завершении" + ANSI_RESET);
                workWithFileClient.sendFileInServer(file, userName, channel);
                break;
            }

            case "11" : {
                System.out.println("Выбрано действие скачать файл, введине название файла     (0 - назад)");
                String fileName = scanner.next();
                if (fileName.equals("0")) { break;}
                command = Command.requestFile(userName, fileName);
                channel.write(command);
                break;
            }
        }


    }




}
