package ru.sviridovaleksey.interactionwithuser;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.network.Connection;

import java.util.Scanner;

public class ChoseAction {

    private Scanner scanner;
    private Channel channel;

    public ChoseAction(Scanner scanner, Channel channel) {
        this.scanner = scanner;
        this.channel = channel;
    }

    public void choseAction (String chose, String userName) {

        Command command;

        switch (chose) {

            case "0" : {
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
                System.out.println("Выбрано действие удалить директорию, введите номер директории:");
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
                System.out.println("Выбрано действие удалить файл, введите номер файла:");
                String numberFile = scanner.next();
                if (numberFile.equals("0")) { break;}
                command = Command.deleteFile(userName, numberFile);
                channel.write(command);
                break;

            }

            case "5" : {
                System.out.println("Выбрано действие копировать файл, введите номер файла:");
                String numberFile = scanner.next();
                if (numberFile.equals("0")) { break;}
                command = Command.copyFile(userName, numberFile);
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
            }


        }

    }

}
