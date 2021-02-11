package ru.daniilazarnov;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler {
    private static final Logger logger = Logger.getLogger (ClientHandler.class.getName ());
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private String nickname;
    private String login;
    private String password;
    private MyCloudFiles file;


    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            file = new MyCloudFiles ();

            server.getExecutorService ().execute (() -> {

                try {
                    //цикл аутентификации
                    while (true) {
                        String str = in.readUTF();

                        if (str.startsWith("/")) {
                            if (str.startsWith("/reg")) {
                                String[] token = str.split("\\s", 4);
                                boolean reg = server.getAuthService()
                                        .registration(token[1], token[2], token[3]);
//                                boolean dir =  file.isNewClientDirCreated (login);
                                //Создаем персональную директорию под нового клиента//
                                if (reg) {
                                    sendCommand ("/regOK");
                                } else {
                                    sendCommand ("/regNO");
                                    System.out.println ("Регистрация не получилась");
                                    break;
                                }
                            }

                            if (str.startsWith("/auth ")) {
                                String[] token = str.split("\\s", 3);
                                String newNick = server.getAuthService()
                                        .getNicknameByLoginAndPassword(token[1], token[2]);
                                if (newNick != null) {
                                    login = token[1];
                                    password = token[2];
                                    if (!server.isloginAuthenticated(login)) {
                                        nickname = newNick;
                                        out.writeUTF("/authok " + nickname);
                                        server.subscribe(this);
                                        break;
                                    } else {
                                        sendCommand ("/authNO ");
                                    }
                                } else {
                                    sendCommAndMsg ("/authNO ","Неверный логин / пароль");
                                }
                            }
                        }
                    }

                    //Цикл работы
                    while (true) {
                        String str = in.readUTF();

                            if (str.startsWith("/ls"))
                            try {
                                String clientFileslist = file.ls (login);
                                sendCommAndMsg ("/lsOK", clientFileslist);
                            } catch (Exception e) {
                                sendCommand ("/lsNO");
                            }

                            if (str.startsWith("/delete")) {
                                String[] token = str.split("\\s+", 2);
                                if (token.length < 2) {
                                    continue;
                                }
                                if (file.isFileDelited (this.login, token[1])){
                                    sendCommAndMsg ("/deleteOK", "Файл успешно удален");
                                } else {
                                    sendCommAndMsg ("/deleteNO", "Ошибка удаления фвйла");
                                }
                            }

                            if (str.startsWith("/receive")) {
                                String[] token = str.split("\\s+", 2);
                                if (token.length < 2) {
                                    continue;
                                }
                                try {
                                    file.receiveFileFromClient (this.in, this.login,token[1]);
                                    sendCommAndMsg ("/receiveOK", "Файл успешно загружен");
                                } catch (Exception e){
                                    sendCommAndMsg ("/receiveNO", "Ошибка передачи файла");
                                }

                            }

                            if (str.startsWith("/sendfile")) {
                                String[] token = str.split("\\s+", 2);
                                if (token.length < 2) {
                                    continue;
                                }
                                try {
                                    file.sendFileToClient (this.out, this.login, token[1]);
                                    sendCommAndMsg ("/sendfileOK", "Файл успешно скачен");
                                } catch (Exception e){
                                    sendCommAndMsg ("/sendfileNO","Ошибка передачи файла" );
                                }
                            }

                            if (str.equals("/end")) {
                                break;
                            }


                    }
                } catch (IOException e) {
                    logger.log (Level.SEVERE,"", e);
                    e.printStackTrace();
                } finally {
                    logger.info ("Client disconnected!");
                    server.unsubscribe(this);
                    try {
                        socket.close();
                    } catch (IOException e) {
                        logger.log (Level.SEVERE,"", e);
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            logger.log (Level.SEVERE,"", e);
            e.printStackTrace();
        }
    }

    public void sendCommand(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendCommAndMsg(String command, String msg) {
        String message = String.format("%s : %s", command, msg);
        sendCommand (message);
    }

    public String getNickname() {
        return nickname;
    }

    public String getLogin() {
        return login;
    }
}
