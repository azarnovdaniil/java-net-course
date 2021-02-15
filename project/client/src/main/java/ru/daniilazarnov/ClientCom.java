package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ClientCom {
    private final Scanner scanner = new Scanner(System.in);

    //Отправка файла на сервер
    void send (ChannelHandlerContext ctx){
        try {
            System.out.println("Укажите путь к файлу, который хотите передать:");
            Path path = Paths.get(scanner.nextLine());
            if (path.toFile().isFile()){
                File file = new File(String.valueOf(path));
                InputStream inputStream = new FileInputStream(file);
                FileMessage fileMessage = new FileMessage(file.getName(), inputStream.readAllBytes());
                System.out.println("Ваш файл сохранён на сервере");
                ctx.writeAndFlush(fileMessage);
                inputStream.close();
            } else {
                System.out.println("Путь к файлу некоректен");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Запрос всех файлов на сервере
    void showFiles (ChannelHandlerContext ctx){
        System.out.println("Доступные файлы:");
        ctx.writeAndFlush("show");
    }

    //Запрос на скачивание файла
    void download (ChannelHandlerContext ctx){
        System.out.println("Введите имя файла, который хотите скачать:");
        ctx.writeAndFlush("dl " + scanner.nextLine());
    }
}
