package ru.daniilazarnov.FileService;

import ru.daniilazarnov.MessagePacket;
import java.io.IOException;
import java.nio.file.*;

//import static ru.daniilazarnov.commandToServer.*;

public class FileManager {

    //поля менеджера файлов
    Path defaultDir = Paths.get("client-storage");// Задаем каталог по-умолчанию откуда пользователь может копировать файлы на сервер, куда по-умолчанию пользователь может скачать файлы с сервера
    Path specificDir = defaultDir; //
    private Enum commandFile;
    private byte[] content;
    int segment;
    int allSegments;

    public FileManager(Path defaultPath, Enum commandFile) {
        this.defaultDir = defaultPath;
        this.commandFile = commandFile;
        this.segment = 1;
        this.allSegments = 1;

    }

    public FileManager() {

    }

    public FileManager(MessagePacket inComingMessage) {
        this.defaultDir= Path.of(inComingMessage.getPathToFileName());
        this.commandFile=inComingMessage;
        this.content=inComingMessage.getContent();
        this.allSegments=inComingMessage.getAllSegments();
        this.segment=inComingMessage.getSegment();
    }

    //Методы менеджера файлов
    // 1) newStorage("путь") - метод задает/создает каталог для работы с сервером, если пользователь решил использовать его вместо каталога "client-storage", метод возвращает объект Path


    Path newStorage(String storagePath) {
            try {
                specificDir = Files.createDirectories(Path.of(storagePath));
            } catch(FileAlreadyExistsException e){
                // Каталог уже существует
            } catch (IOException e) {
                // Что-то пошло не так при чтении/записи
                e.printStackTrace();
            }
                return specificDir;
    } ;

    // 2) fileAction(команда, "путь") - метод принимает на вход команду для работы с файлом, и путь к фалу, возвращает объект Path

    Path fileAction() {
        Path file=null;

        switch (this.commandFile) {
            case this.commandFile.:
               try {
                   Files.write(defaultDir, content, StandardOpenOption.CREATE_NEW);
                   break;
               } catch (IOException e) {
                   e.printStackTrace();
               } finally {
                   return file;
               }
            case REMOVE:
                try {
                    Files.deleteIfExists(fileName); //возвращаемый тип не совпадает с типом метода
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    return file;    //  для случая удаления файла возвращаем null
                }

        }
        return file;
    }

    private void writeInToFile(Path fileName) {
    }
}
