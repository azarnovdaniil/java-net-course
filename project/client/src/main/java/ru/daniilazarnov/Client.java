package ru.daniilazarnov;

import java.io.*;
import java.io.ByteArrayOutputStream;
import java.net.*;

public class Client{

    private final static String fileOutput = "C:\\java_log\\2\\test_out.txt";

    public static void main(String args[]) {
        byte[] aByte = new byte[1];
        int bytesRead;

        Socket clientSocket = null;
        InputStream is = null;

        try {
            clientSocket = new Socket( "localhost" , 8189 );
            is = clientSocket.getInputStream();
        } catch (IOException ex) {
            System.out.println("Ошибка соединения с сервером");

        }

        ByteArrayOutputStream barros = new ByteArrayOutputStream();

        if (is != null) {

            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            try {
                fos = new FileOutputStream( fileOutput );
                bos = new BufferedOutputStream(fos);

                do {
                    barros.write(aByte);
                    bytesRead = is.read(aByte);
                } while (bytesRead != -1);

                bos.write(barros.toByteArray());
                bos.flush();
                bos.close();
                clientSocket.close();
            } catch (IOException ex) {
                System.out.println("Ошибка передачи файла, проверьте доступность сревера!");

            }

            System.out.println("Файл отправлен");
        }

    }

}