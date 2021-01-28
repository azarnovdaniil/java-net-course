
package ru.daniilazarnov.data;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileData extends CommonData implements iData, Serializable {

    private static final long serialVersionUID = 1L;

    private byte[] data;
    private String filename;

    public FileData(TypeMessages type) {
        super(type);
    }

    public void addFile (Path path) {

        if(!Files.exists(path)) {
            System.out.println("Файл не найден!");
        }

        try {
            this.data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
