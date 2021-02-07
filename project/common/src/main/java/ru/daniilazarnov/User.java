package ru.daniilazarnov;

import java.nio.ByteBuffer;

public class User {
    public static int id;
    public String userName;
    public String password;
    public StringBuilder command = new StringBuilder();

    public User(String userName, String password) {
        this.id++;
        this.userName = userName;
        this.password = password;
    }
}
