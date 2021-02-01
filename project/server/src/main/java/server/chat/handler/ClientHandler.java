package server.chat.handler;

import clientserver.Command;
import clientserver.CommandType;
import clientserver.commands.*;
import server.chat.MyServer;

import java.io.*;
import java.net.Socket;
import java.util.Timer;

public class ClientHandler {
    private final MyServer myServer;
    private final Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String userName;
    private String nickName;
    private boolean isAction;

    public ClientHandler(MyServer myServer, Socket clientSocket) {
        this.myServer = myServer;
        this.clientSocket = clientSocket;
        isAction = false;
    }

    public Thread handle() throws IOException {
        in = new ObjectInputStream(clientSocket.getInputStream());
        out = new ObjectOutputStream(clientSocket.getOutputStream());

        return new Thread(() -> {
            try {
                TaskTimeout task = new TaskTimeout(this);
                Timer timer = new Timer();
                timer.schedule(task, 120*1000);
                authentication();
                readMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void authentication() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            if (command.getType() == CommandType.AUTH) {
                isAction = true;
                AuthCommandData data = (AuthCommandData) command.getData();
                String login = data.getLogin();
                String password = data.getPassword();
                this.userName = myServer.getAuthService().getUserName(login, password);

                if (userName != null) {
                    if (myServer.isUserBusy(userName)) {
                        sendMessage(Command.authErrorCommand(userName + " Пользователь уже подключен"));
                    } else {
                        this.nickName = myServer.getAuthService().getNickNameByLogin(login);
                        sendMessage(Command.authOkCommand(userName));
                        myServer.addUser(this);
                        //myServer.broadcastMessage(Command.messageInfoCommand("- "+userName+" присоединился", null), this);
                        return;
                    }
                } else {
                    sendMessage(Command.authErrorCommand("Неверный логин или пароль"));
                }
            } else {
                sendMessage(Command.authErrorCommand("Ошибка авторизации"));
            }
        }
    }

    private Command readCommand() {
        try {
            Object o = in.readObject();
            if (o instanceof Command) {
                return (Command) o;
            } else {
                System.err.println("Получен неизвестный объект");
            }
        } catch (ClassNotFoundException | IOException e) {
            System.err.println("Не удалось получить объект");
            e.printStackTrace();
        }
        return null;
    }

    public String getUserName() {
        return userName;
    }

    public void sendMessage(Command command) throws IOException {
        out.writeObject(command);
    }

    public void readMessage() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command==null) {
                continue;
            }
            switch (command.getType()) {
                case PRIVATE_MESSAGE: {
                    PrivateMessageCommandData data = (PrivateMessageCommandData) command.getData();
                    String name = data.getReceiver();
                    String message = data.getMessage();
                    myServer.personalMessage(Command.messageInfoCommand(message, userName), name);
                    break;
                }
                case END: {
                    closeConnection(true);
                    return;
                }
                case PUBLIC_MESSAGE: {
                    PublicMessageCommandData data = (PublicMessageCommandData) command.getData();
                    String name = data.getSender();
                    String message = data.getMessage();
                    myServer.broadcastMessage(Command.messageInfoCommand(message, name), this);
                    break;
                }
                case CHANGE_LOGIN: {
                    ChangeLoginCommandData data = (ChangeLoginCommandData) command.getData();
                    String login = data.getLogin();
                    String loginNew = data.getLoginNew();
                    if (myServer.getAuthService().changeLogin(login,loginNew)) {
                        // логин изменен успешно
                        sendMessage(Command.changeLoginOkCommand(loginNew));
                        System.out.println("Изменен логин клиента. Старый:"+login+" Новый: "+loginNew);
                    } else {
                        sendMessage(Command.changeLoginErrCommand("Не удалось изменить логин"));
                        System.out.println("Не удалось изменить логин клиента. с "+login+" на: "+loginNew);
                    }
                    break;
                }
                default: {
                    String errMessage = "Неизвестный тип команды "+command.getType();
                    System.err.println(errMessage);
                    sendMessage(Command.errorCommand(errMessage));
                }
            }
        }
    }

    public void closeConnection(boolean notify) {
        myServer.personalMessage(Command.endCommand(), userName);
        try {
            myServer.deleteUser(this);
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Соединение ");
            e.printStackTrace();
        }
    }

    public String getNickName() {
        return nickName;
    }

    public boolean isAction() {
        return isAction;
    }
}
