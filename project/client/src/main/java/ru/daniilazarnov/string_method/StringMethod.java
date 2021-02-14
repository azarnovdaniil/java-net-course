package ru.daniilazarnov.string_method;

public class StringMethod {
    public static final byte THREE_BYTE = 3;
    /**
     * Метод проверяет есть ли второй элемент в строке
     *
     * @param inputLine входные данные
     * @return если есть второй элемент в строке = true
     */
    public static boolean isThereaSecondElement(String inputLine) {
        return inputLine.split(" ").length == 2;
    }

    /**
     * Метод проверяет есть ли третий элемент в строке
     *
     * @param inputLine входные данные
     * @return если есть третий элемент в строке = true
     */
    public static boolean isThereaThirdElement(String inputLine) {
        return inputLine.split(" ").length == THREE_BYTE;
    }

    /**
     * Получает второй элемент ввода
     *
     * @param inputLine входящая строка
     * @return второй элемент строки
     */
    public static String getSecondElement(String inputLine) {
        return inputLine.split(" ")[1];
    }

    /**
     * Получает третий элемент ввода
     *
     * @param inputLine входящая строка
     * @return второй элемент строки
     */
    public static String getThirdElement(String inputLine) {
        return inputLine.split(" ")[2];
    }

}
