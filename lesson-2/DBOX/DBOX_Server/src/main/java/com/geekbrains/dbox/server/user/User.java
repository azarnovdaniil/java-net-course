package com.geekbrains.dbox.server.user;

public class User {
    private String login;
    private String pass;
    private String fio;
    private boolean online = false;
    private int id = 0;

    User(String login, String pass, String fio){
        this.login = login;
        this.pass = pass;
        this.fio = fio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }


    public String getFio() {
        return fio;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
