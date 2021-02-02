package com.geekbrains.dbox.server.user;

import java.util.ArrayList;

public class Users {
    private ArrayList<User> list = new ArrayList<>();

    public void add (String login, String pass, String fio){
        addUser(new User(login, pass, fio));
    }
    public void  del (int idUser){
        list.remove(findUser(idUser));
    }

    private int findUser(int idUser){
        int res = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == idUser) res = i;
        }
        return res;
    }
    private void addUser(User user) {
        user.setId(getMaxId() + 1);
        list.add(user);
    }
    private int getMaxId (){
        int res = 0;
        for (int i = 0; i < list.size(); i++) {
            if(res < list.get(i).getId()) res = list.get(i).getId();
        }
        return res;
    }
}
