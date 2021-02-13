package ru.uio.io.handlers;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class HelpHandler extends BaseHandler{
    public HelpHandler(DataInputStream in, DataOutputStream out) {
        super(in, out);
    }

    @Override
    public void workWithMessage() {
        System.out.println("Команды :\n" +
                "-help                                               Список команд\n" +
                "-auth [логин] [пароль]                              Авторизация на сервере (Пример: -auth n1@mail.com 1)\n" +
                "-l | -list                                          Выводит список файлов\n" +
                "-u | -upload                                        Загрузить файл на сервер\n" +
                "-d | -download [номер файла из списка]              Скачать файл\n" +
                "-exit                                               Отключить клиента от сервера");
    }
}
