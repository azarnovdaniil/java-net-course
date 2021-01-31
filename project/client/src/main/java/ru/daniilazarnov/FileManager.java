package ru.daniilazarnov;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    //поля менеджера файлов
    Path defaultDir = Paths.get("client-storage");// Задаем каталог по-умолчанию откуда пользователь может копировать файлы на сервер, куда по-умолчанию пользователь может скачать файлы с сервера
    Path specificDir = defaultDir; //
    private Enum commandFile;

    public FileManager(Path defaultPath, Enum commandFile) {
        this.defaultDir = defaultPath;
        this.commandFile = commandFile;
    }

    public FileManager() {

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

    Path fileAction(commandFile command, Path fileName) {
        Path file=null;

        switch (command) {
           case CREATE:
               try {
                   file=Files.createFile(fileName);
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
            case WRITE:
                break;
            case READ:
                break;
        }
        return file;
    }

    private void writeInToFile(Path fileName) {
    }
}
