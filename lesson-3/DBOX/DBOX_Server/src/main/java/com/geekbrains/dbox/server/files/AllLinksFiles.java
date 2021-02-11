package com.geekbrains.dbox.server.files;

import com.geekbrains.dbox.server.handlers.in.HandlerInSwitchComand;
import com.geekbrains.dbox.server.user.User;

import java.util.ArrayList;

public class AllLinksFiles {
    private ArrayList<LinkFile> list = new ArrayList<>();

    public void add(User user, String path){
        list.add(new LinkFile(path, user.getLogin(), user.getId(), findFreeId(user.getId())));
    }
    public boolean add(int idUser,String login, String path, String md5){
        boolean res = true;
        if (!checkLink(idUser, path,  md5)){
            list.add(new LinkFile(path, login, idUser, findFreeId(idUser), md5));
        }else res = false;
        return res;
    }
    public boolean checkLink(int idUser, String path, String md5) {
        boolean res = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).realName.equals(path))
               // System.out.println("Check (realName)");
                if (list.get(i).chekSummMD5.equals(md5))
                 //   System.out.println("Check (md5)");
                    if(list.get(i).idUserRoot == idUser)
                  //      System.out.println("Check (idUser)");
                        res = true;
        }
        return res;
    }
    public void print(){
        System.out.println("");
        for (int i = 0; i < list.size(); i++) {
            System.out.println("id - " + list.get(i).getId() + "  NikName - " + list.get(i).getNikname() + " RealName - " + list.get(i).realName + " md5 - " + list.get(i).chekSummMD5);
        }
    }
    public void del(int idUser, String path){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdUserRoot() == idUser)
                if(list.get(i).realName.equals(path)) {
                    list.remove(i);
                    break;
                }
        }
    }
    public void del(LinkFile lf){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdUserRoot() == lf.getIdUserRoot())
                if(list.get(i).realName.equals(lf.realName)) {
                    list.remove(i);
                    break;
                }
        }
    }
    public LinkFile[] getAllLinksUser(int idUser){
        int count =0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdUserRoot() == idUser) count++;
        }

        LinkFile[] lf = new LinkFile[count];
        int lfindex = 0;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdUserRoot() == idUser) lf[lfindex] = list.get(i);
        }
        return lf;
    }
    public LinkFile getLinkUser(int idUser, String path){
        LinkFile lf = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdUserRoot() == idUser)
                if(list.get(i).realName.equals(path)) lf = list.get(i);
        }
        return lf;
    }


    private int findFreeId(int idUser){
        int z = 0;
        while (findId(idUser, z)){
            z++;
        }
        return z;
    }

    private boolean findId(int idUser, int idFind){
        boolean res = false;
        for (int i = 0; i < list.size(); i++) {
            if (idUser == list.get(i).getIdUserRoot()){
                if (idFind == list.get(i).getId()) res = true;

            }
        }
        return res;
    }
}
