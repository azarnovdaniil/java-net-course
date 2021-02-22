package ru.daniilazarnov.auth;

import java.util.List;


public class BaseAuthService {

    private static final List<User> clients = List.of(
            new User("user1"),
            new User("user2"),
            new User("user3")
    );


    public String checkingByLoginAndPassword(String login, String password) {
        for (User client : clients) {
            if(client.getLogin().equals(login))/* & client.getPassword().equals(password) ) */{
                return client.getLogin();
            }
        }
        return null;
    }
}
