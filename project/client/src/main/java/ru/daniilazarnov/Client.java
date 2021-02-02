package ru.daniilazarnov;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 8190;
    private final static String fileOutput = "C:\\temp\\2.txt";

    public static void main(String args[]) {
        byte[] aByte = new byte[1];
        int bytesRead;

        Socket clientSocket = null;
        InputStream is = null;

        try {
            clientSocket = new Socket( HOST , PORT );
            is = clientSocket.getInputStream();
        } catch (IOException ex) {
            System.out.println("Клиент не смог подключиться к серверу");
            System.out.println("Проверьте запущен ли сервер");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (is != null) {
            System.out.println("Клиент подключился к серверу");
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            try {
                fos = new FileOutputStream( fileOutput );
                bos = new BufferedOutputStream(fos);
                bytesRead = is.read(aByte, 0, aByte.length);
                System.out.println("Клиент считал с сервера поток данных в массив aByte");
                do {
                    baos.write(aByte);          // пишем из массива aByte в поток baos
                    bytesRead = is.read(aByte); // читаем из потока is в массив байт aByte до тех пор пока поток не опустеет
                } while (bytesRead != -1);

                bos.write(baos.toByteArray());  // пишем считанный поток baos в файл fileOutput
                bos.flush();
                bos.close();
                clientSocket.close();
            } catch (IOException ex) {
                System.out.println("Возникли проблемы при записи файла");
            }
        }
    }

}
