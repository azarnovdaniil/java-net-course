package ru.daniilazarnov;

//import java.io.Serializable;

import java.io.Serializable;

/**
 * Class MessagePacket определяет универасльный пакет - содержание (состав) сообщений, пересылаемых между клиентом и сервер.
 * Каждое собщение состоит из:
 *  - хэш для унификации клиентов: (int) PASSWORD * PASSWORD.length*Pi
 *  - команды к серверу (Enum),
 *  - путь для сохранения файла, включая имя файла (String) (для простоты реалзиации - "имя клиента / имя файла")
 *  - содержимое для записи в файл (String) (для простоты реализации)
 *  - число дробных частей (int), на которые разбито "сообщение"
 *  - номер дробной части (int), передаваемое в данный момент
 */
public class MessagePacket implements Serializable {
    private final String PASSWORD="name";
    final private int hash;
    private Commands command;
    private String pathToFileName;
    private byte[] content;
    private int segment;
    private int allSegments;

    public void setSegment(int segment) {
        this.segment = segment;
    }

    public void setAllSegments(int allSegments) {
        this.allSegments = allSegments;
    }



    public int getSegment() {
        return segment;
    }

    public int getAllSegments() {
        return allSegments;
    }

    public String getPathToFileName() {
        return pathToFileName;
    }

    public void setPathToFileName(String pathToFileName) {
        this.pathToFileName = pathToFileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public MessagePacket() {
        this.hash = (int) (Integer.parseInt(PASSWORD)+(PASSWORD.length()*Math.PI));
        this.command = null;
        this.pathToFileName = "";
        this.content = new  byte[1024 * 1024 * 100];
        this.segment = 0;
        this.allSegments = 0;
    }

    public MessagePacket(String  PASSWORD, Commands command, String pathToFileName, byte[] content, int segment, int allSegments) {
        this.hash =  (int) (Integer.parseInt(PASSWORD)+(PASSWORD.length()*Math.PI));
        this.commandToServer = commandToServer;
        this.pathToFileName = pathToFileName;
        this.content = content;
        this.segment = segment;
        this.allSegments = allSegments;
    }
}
