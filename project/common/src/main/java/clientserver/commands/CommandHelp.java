package clientserver.commands;

import clientserver.FileLoaded;
import clientserver.FileWorker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Map;

public class CommandHelp implements Command {
    private final List<String> list = List.of(
            "Приложение предоставляет доступ к облачному хранилищу.",
            "Перед курсором выводится полный текущий локальный путь.",
            "",
            "Операции на локальном ПК:",
            "",
            "lls - показать список файлов и директорий в текущей директории",
            "lcd <имя директории> - перейти в директорию с указанным именем",
            "exit - выход из приложения",
            "",
            "Операции в облаке:",
            "",
            "ls - показать список файлов и директорий в текущей директории",
            "cd <имя директории> - перейти в директорию с указанным именем",
            "rm <имя файла или директории> - удалить файл или директорию (если директория не пустая, она не будет удалена)",
            "mkdir <имя директории> - создать директорию с указанным именем",
            "mv <имя файла> <имя директории> - переместить файл в указанную директорию. Директория указывается от текущей.",
            "rename <имя файла> <новое имя файла> - переименовать файл",
            "",
            "Загрузка/выгрузка файлов:",
            "",
            "upload <имя файла из текущей локальной директории> - загрузить указанный файл в облако",
            "download <имя файла из текущей директории в облаке> - скачать указанный файл. Файл будет скачан в текущую директорию",
            "");

    @Override
    public void send(ChannelHandlerContext ctx, String content, FileWorker fileWorker, byte signal) {
        list.forEach(System.out::println);
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer, FileLoaded> uploadedFiles, byte signal) {

    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer, FileLoaded> uploadedFiles) {

    }
}
