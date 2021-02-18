package ru.daniilazarnov;

import java.io.Serializable;

public class User implements Serializable {
    private static int id;
    private String userName;
    private String password;
    public User(String userName, String password) {
        this.id++;
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
