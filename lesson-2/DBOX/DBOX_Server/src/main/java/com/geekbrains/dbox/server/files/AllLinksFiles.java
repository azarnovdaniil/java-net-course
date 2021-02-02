package com.geekbrains.dbox.server.files;

import com.geekbrains.dbox.server.user.User;

import java.util.ArrayList;

public class AllLinksFiles {
    private ArrayList<LinkFile> list = new ArrayList<>();

    public void add(User user, String path){
        list.add(new LinkFile(path, user.getLogin(), user.getId(), findFreeId(user.getId())));
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
