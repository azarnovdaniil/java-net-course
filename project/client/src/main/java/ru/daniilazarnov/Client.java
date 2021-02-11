package ru.daniilazarnov;

public class Client {

     private static boolean cycleOn = true;
    private static final int U = 1;
    private static final int D = 2;
    private static final int DE = 3;
    private static final int V = 4;
    private static final int Q = 5;

    public static void main(String[] args) {
        Commands.printMessage("Добрый день! \nВведите логин и пароли "); // Проверка log1 pass123 ; log2 pass456 ; log3 pass789
        do {
            Controller.doAuthorization();
        } while (Controller.getName() == null);
        do {
            Commands.printMessage("Введите команду:\n1.Загрузить на сервер\n2.Скачать с сервера\n3.Удалить\n4.Список файлов\n0.Выход");
            int answer = Commands.getMenuItems();
            switch (answer) {
                case (U) -> Controller.upload();
                case (D) -> Controller.download();
                case (DE) -> Controller.delete();
                case (V) -> Controller.view();
                case (Q) -> quit();
            }
        } while (cycleOn);
    }

    private static void quit() {
        Commands.printMessage("До встречи!");
        cycleOn = false;
    }
}
