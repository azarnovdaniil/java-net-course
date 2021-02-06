package ru.daniilazarnov;

//import java.io.Serializable;

import java.io.Serializable;

/**
 * Class MessagePacket определяет универасльный пакет - содержание (состав) сообщений, пересылаемых между клиентом и сервер.
 * Каждое собщение состоит из:
 * - команды к серверу (Enum),
 * - путь для сохранения файла, включая имя файла (String) (для простоты реалзиации)
 *  - содержимое для записи в файл (String) (для простоты реализации)
 */
public class MessagePacket implements Serializable {
    private commandToServer commandToServer;
    private String pathToFileName;
    private String contentMessage;

    public ru.daniilazarnov.commandToServer getCommandToServer() {
        return commandToServer;
    }

    public void setCommandToServer(ru.daniilazarnov.commandToServer commandToServer) {
        this.commandToServer = commandToServer;
    }

    public String getPathToFileName() {
        return pathToFileName;
    }

    public void setPathToFileName(String pathToFileName) {
        this.pathToFileName = pathToFileName;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public void setContentMessage(String contentMessage) {
        this.contentMessage = contentMessage;
    }


}
