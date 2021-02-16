package ru.sviridovaleksey.interactionwithuser;

import io.netty.channel.Channel;
import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.workwithfile.WorkWithFileClient;
import java.io.File;
import java.util.Scanner;

public class ChoseAction {

    private final Scanner scanner = new Scanner(System.in);
    private final Channel channel;
    private final WorkWithFileClient workWithFileClient;


    public ChoseAction(Channel channel, WorkWithFileClient workWithFileClient) {
        this.channel = channel;
        this.workWithFileClient = workWithFileClient;
    }

    public void choseAction(String chose, String userName) {

        Command command;


        if (chose.equals("0")) {
            return;
        } else if (chose.equals("a")) {
                command = Command.getShowDir(userName, "");
                channel.write(command);

            } else if (chose.equals("1")) {
                System.out.println("Выбрано действие создать директорию, введите название директории:");
                String nameDirectory = scanner.next();
                if (nameDirectory.equals("0")) {
                    return;
                }
                command = Command.createNewDirectory(userName, nameDirectory);
                channel.write(command);

            } else if (chose.equals("2")) {
                System.out.println("Выбрано действие удалить директорию, введите имя директории:");
                String numberDirectory = scanner.next();
                if (numberDirectory.equals("0")) {
                    return;
                }
                command = Command.deleteDirectory(userName, numberDirectory);
                channel.write(command);

            } else if (chose.equals("3")) {

                System.out.println("Выбрано действие создать файл, введите имя файла:");
                String fileName = scanner.next();
                if (fileName.equals("0")) {
                    return;
                }
                command = Command.createNewFile(userName, fileName);
                channel.write(command);

            } else if (chose.equals("4")) {

                System.out.println("Выбрано действие удалить файл, введите имя файла:");
                String nameFile = scanner.next();
                if (nameFile.equals("0")) {
                    return;
                }
                command = Command.deleteFile(userName, nameFile);
                channel.write(command);

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
                channel.write(command);

            }  else if (chose.equals("7")) {
                System.out.println("Выбрано действие отправить сообщение на сервер, введите сообщение");
                String message = scanner.next();
                if (message.equals("0")) {
                    return;
                }
                command = Command.message(userName, message);
                channel.write(command);

            } else if (chose.equals("8")) {
                System.out.println("Выбрано действие открыть директорию, введите название директории");
                String message = scanner.next();
                if (message.equals("0")) {
                    return;
                }
                command = Command.getShowDir(userName, message);
                channel.write(command);

            } else if (chose.equals("9")) {
                System.out.println("Выбрано действие 'назад'");
                command = Command.getBackDir(userName, "");
                channel.write(command);

            } else if (chose.equals("10")) {
                System.out.println("Выбрано действие отправить файл на серер");
                System.out.println("Введите путь к файлу (пример D:/test.txt) :    (0 - назад)");
                String way = scanner.next();
                if (way.equals("0")) {
                    return;
                }
                File file = new File(way);
                command = Command.createNewFile(userName, file.getName());
                channel.write(command);
                String ansirest = "\u001B[0m";
                String ansired = "\u001B[31m";
                System.out.println(ansired + "Дождитесь ответа от сервера о завершении" + ansirest);
                workWithFileClient.sendFileInServer(file, userName, channel);

            } else if (chose.equals("11")) {
                System.out.println("Выбрано действие скачать файл, введине название файла     (0 - назад)");
                String fileName = scanner.next();
                if (fileName.equals("0")) {
                    return;
                }
                command = Command.requestFile(userName, fileName);
                channel.write(command);
            }
        }
    }
