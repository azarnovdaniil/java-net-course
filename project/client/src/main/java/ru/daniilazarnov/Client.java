package ru.daniilazarnov;

public class Client {

    private static boolean cycleOn = true;
    private static final int U = 1;
    private static final int D = 2;
    private static final int DE = 3;
    private static final int V = 4;
    private static final int Q = 5;

    public static void main(String[] args) throws Exception {
        ClientCommands.printMessage("Добрый день! \nВведите логин и пароли ");
        do {
            ClientController.doAuthorization();
        } while (ClientController.getName() == null);
        do {
            ClientCommands.printMessage("Введите команду:\n1.Загрузить на сервер\n2.Скачать с сервера\n3.Удалить\n4.Список файлов\n0.Выход");
            int answer = ClientCommands.getMenuItems();
            switch (answer) {
                case (U) -> ClientController.upload();
                case (D) -> ClientController.download();
                case (DE) -> ClientController.delete();
                case (V) -> ClientController.view();
                case (Q) -> quit();
            }
        } while (cycleOn);
    }

    private static void quit() {
        ClientCommands.printMessage("До встречи!");
        cycleOn = false;
    }
}