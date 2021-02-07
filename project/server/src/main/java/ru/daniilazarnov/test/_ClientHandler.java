package ru.daniilazarnov.test;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Logger;

public class _ClientHandler {
    private _MyServer server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private BufferedInputStream inFile;
    private BufferedOutputStream outFile;
    private final static String fileToSend = "project/server/files/test1.txt";
    private static final Logger logger = Logger.getLogger(_ClientHandler.class.getName());
    private String nickname;
    private String login;
    private BufferedOutputStream outToClient = null;

    public _ClientHandler(_MyServer server, Socket socket) {

        try {
            outToClient = new BufferedOutputStream(socket.getOutputStream());
            while (true) {
                if (outToClient != null) {
                    System.out.println("Установлено подключение, готов отдавать файл...");
                    File myFile = new File(fileToSend);
                    byte[] mybytearray = new byte[(int) myFile.length()];
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(myFile);      // считали в поток данные из файла, лежащего на сервере
                    } catch (FileNotFoundException ex) {
                        // Do exception handling
                    }
                    BufferedInputStream bis = new BufferedInputStream(fis);

                    try {
                        bis.read(mybytearray, 0, mybytearray.length);
                        outToClient.write(mybytearray, 0, mybytearray.length);
                        outToClient.flush();
                        outToClient.close();
                        socket.close();

                        // File sent, exit the main method
                        return;
                    } catch (IOException ex) {
                        System.out.println("Ошибка при получении");
                    }
                }
            }
        } catch (IOException ex) {
            // Do exception handling
        }


        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            inFile = new BufferedInputStream(socket.getInputStream());
            outFile = new BufferedOutputStream(socket.getOutputStream());

            server.getExecutorService().execute(() -> {
                try {
                    socket.setSoTimeout(120000);

                    //Цикл работы через in|out
                    while (true) {
                        String str = in.readUTF();

                        if (str.startsWith("/")) {
                            if (str.equals("/end")) {
                                out.writeUTF("/end");
                                break;
                            }
                        } else {
                            logger.info(nickname + " отправил сообщение");
                        }
                    }
                } catch (SocketTimeoutException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Client disconnected!");
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
