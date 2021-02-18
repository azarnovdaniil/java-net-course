package ru.daniilazarnov.auth;

import ru.daniilazarnov.entity.User;
import java.util.ArrayList;
import java.util.List;

public class BasicAuthService implements AuthService{
    private static List<User> users = new ArrayList<>();

    @Override
    public void addUser(User user) {
        users.add(user);
    }


    @Override
    public User doAuth(String login, String password) {
        for (User user : users) {
            if (user.getNickname().equals(login) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean doReg(String login, String password) {
        Boolean isExist=false;
        for (User user : users) {
            if (user.getNickname().equals(login) && user.getPassword().equals(password)) {
                isExist=true;
            }
        }
        return isExist;
    }
}
