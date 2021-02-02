package com.geekbrains.dbox.server.files;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class LinkFile {
    String realName;
    private String nikname;
    private int id;
    String chekSummMD5;
    int idUserRoot;
    private ArrayList<Integer> listUsers = new ArrayList<>();

    LinkFile(String nameReal, String nikname, int idUserRoot, int idFile){
        id = idFile;
        realName = nameReal;
        this.nikname = nikname;
        this.idUserRoot = idUserRoot;
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
