package ru.sviridovaleksey.clientHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataBaseUser {

    private String[] userLogin = {"user1", "user2", "user3"};
    private String[] userPassword ={"111", "222", "333"};
    private List<String> login = new ArrayList<>();

    public DataBaseUser() {
        login = Arrays.asList(userLogin);
    }




    public String getUserNameByLogin(String login, String password){
        if (this.login.contains(login)) {
            int adress = this.login.indexOf(login);
            if (userPassword[adress].equals(password)) {
                return login;
            }
        }

        return null;
    }

}
