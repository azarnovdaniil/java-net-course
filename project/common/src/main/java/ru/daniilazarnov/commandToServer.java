package ru.daniilazarnov;

public enum commandToServer {
    //комманды для использования в файлменеджере для СОЗДАНИЯ файла, УДАЛЕНИЯ файла, ЗАПИСИ в файл, ЧТЕНИЯ файла
    CREATE(1), REMOVE(2),WRITE(3), READ(4);
    private int numberOFCommand;

    commandToServer(int numberOFCommand) {
        this.numberOFCommand = numberOFCommand;
    }

    public int getNumberOFCommand() {
        return numberOFCommand;
    }
}
