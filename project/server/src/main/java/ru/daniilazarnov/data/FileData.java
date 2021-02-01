
package ru.daniilazarnov.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileData extends CommonData implements iData {

    private static final long serialVersionUID = 1L;

    private String hash;
    private String filename;
    private String calalog;
    private String fromPath;
    private long lengthByte;

    public FileData(TypeMessages type) {
        super(type);
    }

    public void setCalalog(String calalog){
        this.calalog = calalog;
    }

    public String getFromPath(){
        return this.fromPath;
    }

    public String getToCatalog(){
        return this.calalog;
    }

    public long getLengthByte () {
        return this.lengthByte;
    }

    public void addFile (Path path) {

        StringBuilder resultHash = new StringBuilder();
        if(!Files.exists(path)) {
            System.out.println("Файл не найден!");
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            DigestInputStream dis = new DigestInputStream(Files.newInputStream(path), md);

            for (byte b : md.digest()) {
                resultHash.append(String.format("%02x", b));
            }

            this.hash = resultHash.toString();
            this.filename = path.getFileName().toString();
            this.fromPath = path.toString();
            this.lengthByte = new File(path.toString()).length();

        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

    }
}
