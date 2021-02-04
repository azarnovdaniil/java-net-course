package ru.sviridovaleksey.clientHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class dataBaseUser {

    private String[] userLogin = {"user1", "user2", "user3"};
    private String[] userPassword ={"111", "222", "333"};
    private List<String> login = new ArrayList<>();

    public dataBaseUser() {
        login = Arrays.asList(userLogin);
    }




    public String getUserNameByLogin(String login, String password){

        int adress = login.indexOf(login);
        if (userPassword[adress].equals(password)) {
            return login;
        }

        return null;
    }

}
