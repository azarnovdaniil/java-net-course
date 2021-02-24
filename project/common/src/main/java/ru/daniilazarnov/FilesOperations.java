package ru.daniilazarnov;
import java.io.*;

public class FilesOperations {

    // метод копирования массива байтов в файл: должен передаваться полный относительный (проекта) путь к файлу
    // например: project/client/files/jon/jon1.txt
    public static void saveToFileFromByteArr(byte[] aByte, String filename) {
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(filename))) {
            out.write(aByte);
            out.flush();
        } catch (IOException e) {
            System.out.println("Возникли проблемы при записи файла");
            e.printStackTrace();
        }
    }

    // метод копирования файла в массив байтов: должен передаваться полный относительный (проекта) путь к файлу
    // например: project/server/files/jon/jon1.txt
    public static byte[] readFromFileToByteArr(String filename) {
        File myFile = new File(filename);
        byte[] aByte = new byte[(int) myFile.length()];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(myFile);      // открываем файл
        } catch (FileNotFoundException ex) {
            System.out.println("Ошибка при чтении клиентского файла" + filename);
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedInputStream bis = new BufferedInputStream(fis);         // подключаем буфферизацию
        try {
            bis.read(aByte, 0, aByte.length);           // читаем файл в массив
        } catch (IOException ex) {
            System.out.println("Ошибка при записи клиентского файла в массив");
        } finally {
            try {
                fis.close();
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return aByte;
    }
}
