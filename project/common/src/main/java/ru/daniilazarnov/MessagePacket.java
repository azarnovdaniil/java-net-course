package ru.daniilazarnov;
import ru.daniilazarnov.commands.Commands;
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
    private String pathCode;
    private String fileName;

    public String getHomeDirectory() {
        return homeDirectory;
    }

    private String homeDirectory;
    private byte[] content;
    private int segment;
    private int allSegments;
    public Commands getCommand() {
        return command;
    }

    public void setCommand(Commands command) {
        this.command = command;
    }

    private Commands command;

    public String getPathCode() {
        return pathCode;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getSegment() {
        return segment;
    }

    public int getAllSegments() {
        return allSegments;
    }


    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public MessagePacket() {
        this.command = null;
        this.content = new  byte[1024 * 1024 * 100];
        this.segment = 1;
        this.allSegments = 1;
    }
 public MessagePacket(String name, String clientKey) {
     this.homeDirectory = name.toLowerCase().trim();
     new MessagePacket();
     this.pathCode = (name+ clientKey.length() *Math.PI+clientKey).replace('.', '-');
 }

}
