package com.geekbrains.dbox.server.files;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class LinkFile {
    //Данные по скачиванию файла;
    public int colPak = 0;

    String realName;                                                            //Имя файла на лок. комп.
    private String nikname;                                                     //Имя файла на сервере
    private int id;                                                             //id файла
    String chekSummMD5;                                                         //Контрольная сумма
    int idUserRoot;//id пользователя правообладателя
    private ArrayList<Integer> listUsers = new ArrayList<>();                   //Пользователи которым разрешен доступ

    LinkFile(String nameReal, String nikname, int idUserRoot, int idFile){
        id = idFile;
        realName = nameReal;
        this.nikname = nikname;
        this.idUserRoot = idUserRoot;
    }
    LinkFile(String nameReal, String nikname, int idUserRoot, int idFile, String md5){
        id = idFile;
        realName = nameReal;
        this.nikname = nikname;
        this.idUserRoot = idUserRoot;
        chekSummMD5 = md5;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }

    public int getIdUserRoot() {
        return idUserRoot;
    }

    public int getId() {
        return id;
    }

    public String getNikname() {
        return nikname + id;
    }

    public void setNikname(String nikname, int id) {
        this.nikname = nikname;
        this.id = id;
    }

    public void addUser (int idUser) {
        listUsers.add(idUser);
    }
    public void delUser (int idUser) {
        listUsers.remove(findIdUser(idUser));
    }


    private int findIdUser(int idUser){
        int res =0;
        for (int i = 0; i < listUsers.size(); i++) {
            if(idUser == listUsers.get(i)) res = i;
        }
        return res;
    }

}
