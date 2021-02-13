package ru.daniilazarnov;

public class ClientLogin {

    private static String login;
    private static String password;


    public void setClientLogin(String login, String password){

        this.login = login;
        this.password = password;

    }

    public static String getLogin(){

        return login;
    }

    public static String getPassword(){
        return password;
    }
}
