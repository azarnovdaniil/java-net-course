package ru.daniilazarnov.auth;

import java.util.List;


public class BaseAuthService implements AuthService {

    private static final List<User> clients = List.of(
            new User("user1", "111"),
            new User("user2", "222"),
            new User("user3", "333")
    );


    @Override
    public String getUsernameByLoginAndPassword(String login, String password) {
        return null;
    }
}
