package ru.daniilazarnov;

//import java.io.Serializable;

import java.io.Serializable;

/**
 * Class MessagePacket определяет универасльный пакет - содержание (состав) сообщений, пересылаемых между клиентом и сервер.
 * Каждое собщение состоит из:
 *  - команды к серверу (Enum),
 *  - путь для сохранения файла, включая имя файла (String) (для простоты реалзиации - "имя клиента / имя файла")
 *  - содержимое для записи в файл (String) (для простоты реализации)
 *  - число дробных частей (int), на которые разбито "сообщение"
 *  - номер дробной части (int), передаваемое в данный момент
 */
public class MessagePacket implements Serializable {
    private commandToServer commandToServer;
    private String pathToFileName;
    private byte content;
    private int segment;
    private int allSegments;


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

    public byte getContent() {
        return content;
    }

    public void setContent(byte content) {
        this.content = content;
    }

    public MessagePacket(ru.daniilazarnov.commandToServer commandToServer, String pathToFileName, byte content, int segment, int allSegments) {
        this.commandToServer = commandToServer;
        this.pathToFileName = pathToFileName;
        this.content = content;
        this.segment = segment;
        this.allSegments = allSegments;
    }
}
