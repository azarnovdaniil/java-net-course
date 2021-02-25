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
                        String str = in.readUTF ();
                        if (str.startsWith ("/reg")) {
                            String[] token = str.split ("\\s", 4);
                            boolean reg = server.getAuthService ()
                                    .registration (token[1], token[2], token[3]);
                            boolean dir = file.isNewClientDirCreated (token[1]);
//                                Создаем персональную папку под нового клиента в директории myclaud//
                            if (reg && dir) {
                                sendInfo ("/regOK");
                                logger.info ("Directory for client " + token[1] + " in dir mycloud is created!");
                                login = token[1];
                                break;

                            } else {
                                sendInfo ("/regNO");
                                System.out.println ("Регистрация не получилась");
                                break;
                            }
                        }

                        if (str.startsWith ("/auth")) {
                            String[] token = str.split ("\\s", 3);
                            String newNick = server.getAuthService ()
                                    .getNicknameByLoginAndPassword (token[1], token[2]);
                            if (newNick != null) {
                                login = token[1];
                                password = token[2];
                                if (!server.isloginAuthenticated (login)) {
                                    nickname = newNick;
                                    sendInfo ("/authOK " + nickname);
                                    server.subscribe (this);
                                    logger.info ("Client " + token[1] + " auth OK");
                                    break;
                                } else {
                                    sendInfo ("/authNO ");
                                    break;
                                }
                            } else {
                                sendInfo ("/authNO ");
                                break;
                            }
                        }

                    }

                    //Цикл работы
                    while (true) {
                        String str = in.readUTF ();
                        if (str.startsWith ("/ls")) {
                            try {
                                String clientFileslist = file.ls (this.login);
                                sendInfo (clientFileslist);

                            } catch (Exception e) {
                                logger.info ("command -ls- failed");
                                sendInfo ("Не удалось загрузить список файлов.");
                            }
                        }

                        if (str.startsWith ("/rename")) {
                            String[] token = str.split ("\\s+", 3);
                            if (token.length < 3) {
                                continue;
                            }
                            if (file.isFileRenamed (this.login, token[1], token[2])) {
                                sendInfo ("Файл успешно переименован.");
                            } else {
                                sendInfo ("Не удалось переименовать файл.");
                            }
                        }
                        if (str.startsWith ("/delete")) {
                            String[] token = str.split ("\\s+", 2);
                            if (token.length < 2) {
                                continue;
                            }
                            if (file.isFileDelited (this.login, token[1])) {
                                sendInfo ("Файл успешно удален.");
                            } else {
                                sendInfo ("Ошибка удаления файла.");
                            }
                        }


                        if (str.startsWith ("/upload")) {
                            String[] token = str.split ("\\s+", 2);
                            if (token.length < 2) {
                                continue;
                            }
                            if (file.isFileExists (this.login, token[1])) {
                                try {
                                    file.sendFileToClient (this.out, this.login, token[1]);
                                    sendInfo ("Файл успешно скачен.");
                                    logger.info ("command -upload- OK");
                                } catch (Exception e) {
                                    logger.info ("command -upload- failed");
                                    sendInfo ("Ошибка передачи файла.");
                                }
                            } else {
                                sendInfo ("В Облаке нет файла с таким именем.");
                            }

                        }

                        if (str.equals ("/end")) {
                            break;
                        }

                    }
                } catch (IOException e) {
                    logger.log (Level.SEVERE,"", e);
                    e.printStackTrace();
                } finally {
                    logger.info ("Client " + this.login + " disconnected!");
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

    public void sendInfo(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }

    public String getLogin() {
        return login;
    }
}
