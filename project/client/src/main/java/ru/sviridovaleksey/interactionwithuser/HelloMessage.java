package ru.sviridovaleksey.interactionwithuser;

public class HelloMessage {

    public HelloMessage() {
        System.out.println("Добро пожаловать в хранилище файлов. Устанавливаем соединение с сервером");
    }

    public void helloMessage() {
        System.out.println("Выбирете действие:");
        System.out.println("0 - отменить действие; a - обновить; 1 - создать директорию; "
                + "2 - удалить директорию; 3 - создать файл; 4 - удалить файл");
        System.out.println("5 - Переименовать файл; 7 - отправить сообщение на сервер; "
                + "8 - открыть директорию; 9 - назад; 10 - отправить файл на сервер; 11 - скачать файл");
    }

    public void  needRegMessage() {

        System.out.println("Для входа в систему введите логин и пароль через enter");

    }


}
