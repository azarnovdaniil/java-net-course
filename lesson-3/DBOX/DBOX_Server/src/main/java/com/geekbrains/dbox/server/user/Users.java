package com.geekbrains.dbox.server.user;

import java.util.ArrayList;

public class Users {
    private  ArrayList<User> list = new ArrayList<>();

    public  void add (String login, String pass, String fio){
        addUser(new User(login, pass, fio));
    }
    public  void  del (int idUser){
        list.remove(findUser(idUser));
    }
    public  int[] auth(String login, String password){
        int[] res = new int[3];
        // res[0] - 1 - true
//                res[1] - idUser
        // res[0] - 0 - false
//                res[1] - login (0 - pass, 1 - login )
        if(findLogin(login)) {
            if(chekPass(login, password)) {
                    int ui = getUserId(login, password);
                    if (auth(ui)) {
                        res[0] = 1;
                        res[1] = ui;
                    } else {
                        res[0] = 0;
                        res[1] = 0;
                    }
            }else {
                res[0] = 0;
                res[1] = 0;
            }
        }else {
            res[0] = 0;
            res[1] = 1;
        }

    return res;
    }
    public  boolean auth(int idUser){
        boolean re = false;
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getId() == idUser) {
                list.get(i).setOnline(true);
                re = true;
            }
        }
        return re;
    }
    public void setOnline (int idUser){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == idUser) list.get(i).setOnline(true);
        }
    }
    public  int getUserId (String login, String pass){
        int re = -1;
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getLogin().equals(login))
                if (list.get(i).getPass().equals(pass)) re = list.get(i).getId();
        }
        return re;
    }
    public  boolean findLogin (String login){
        boolean re = false;
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getLogin().equals(login)) re =true;
        }
        return re;
    }
    public  boolean chekPass (String login, String pas){
        boolean re = false;
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getLogin().equals(login))
                if(list.get(i).getPass().equals(pas)) re = true;
        }
        return re;
    }
    public boolean chekAuth (int idUser) {
        boolean res = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == idUser) res = list.get(i).getOnline();
        }
        return res;
    }
    public  User getUser_Id (int idUser){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == idUser) return list.get(i);
        }
        return null;
    }
    public int getSize(){
        return list.size();
    }
    private  int findUser(int idUser){
        int res = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == idUser) res = i;
        }
        return res;
    }
    private  void addUser(User user) {
        user.setId(getMaxId() + 1);
        list.add(user);
    }
    private  int getMaxId (){
        int res = 0;
        for (int i = 0; i < list.size(); i++) {
            if(res < list.get(i).getId()) res = list.get(i).getId();
        }
        return res;
    }

    public void print(){
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).getFio() + " : " + list.get(i).getLogin() + " : " + list.get(i).getPass() );
        }
    }

}
