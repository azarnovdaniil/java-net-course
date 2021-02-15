package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class ServerCom {
    //Сохранение файла на сервере
    void save(FileMessage fileMessage){
        System.out.println("Скачивание файла...");
        try {
            Files.write(Path.of("C:\\Users\\Роман\\Desktop\\java-net-course\\project\\server\\src\\Files"
                            + File.pathSeparator + fileMessage.getFileName()),
                    fileMessage.getContent(),
                    StandardOpenOption.CREATE_NEW);
            System.out.println("Файл скачан!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Вывод на экран всех файлов на сервере
    void show(ChannelHandlerContext ctx){
        File dir = new File("C:\\Users\\Роман\\Desktop\\java-net-course\\project\\server\\src\\Files");
        for (File file : Objects.requireNonNull(dir.listFiles())){
            ctx.writeAndFlush(file.getName());
        }
    }

    //Скачивание файла на клиент
    void download(ChannelHandlerContext ctx, String fileName) {
        try{
            File file = new File("C:\\Users\\Роман\\Desktop\\java-net-course\\project\\server\\src\\Files"
                    + File.pathSeparator + fileName);
            if (file.exists()){
                InputStream inputStream = new FileInputStream(file);
                FileMessage fileMessage = new FileMessage(file.getName(), inputStream.readAllBytes());
                System.out.println("Отправка файла...");
                ctx.writeAndFlush(fileMessage);
                inputStream.close();
            } else ctx.writeAndFlush("Такого файла не существует");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
