package ru.daniilazarnov;

import java.io.*;
import java.net.Socket;

public class FilesOperations {
    private final static String clientfileToSave = "project/client/files/server.txt";
    private final static String clientfile = "project/client/files/client.txt";
    private final static String serverfile = "project/server/files/server.txt";

    public static void saveToFileFromByteArr(byte[] aByte, String filename) {

//        try (FileOutputStream out = new FileOutputStream(fileToSave)) {
//            out.write(aByte);
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Возникли проблемы при записи файла");
//        }

        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(filename))) {
            out.write(aByte);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        FileOutputStream fos = null;
//        BufferedOutputStream bos = null;
//        try {
//            fos = new FileOutputStream( fileToSave );
//            bos = new BufferedOutputStream(fos);
//            bos.write(aByte);
//            bos.flush();
//            bos.close();
//        } catch (IOException ex) {
//            System.out.println("Возникли проблемы при записи файла");
//        }

    }

    public static byte[] readFromFileToByteArr(String filename) {
        File myFile = new File(filename);
        byte[] aByte = new byte[(int) myFile.length()];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(myFile);      // открыли файл, лежащий на клиенте
        } catch (FileNotFoundException ex) {
            System.out.println("Ошибка при чтении клиентского файла" + filename);
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedInputStream bis = new BufferedInputStream(fis);         // подключили буфферизацию

        try {
            bis.read(aByte, 0, aByte.length);           // считали файл в массив
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
