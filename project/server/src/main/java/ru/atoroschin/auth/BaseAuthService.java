package ru.atoroschin.auth;

import ru.atoroschin.AuthService;

import java.util.List;

public class BaseAuthService implements AuthService {

    private final List<User> users = List.of(new User(1, "user1", "1", "user_1"),
            new User(2, "user2", "1", "user_2"),
            new User(3, "user3", "1", "user_3"),
            new User(-1, "unknown","", "unknown"));

    @Override
    public int getUserID(String login, String pass) {
        for (User user : users) {
            if (user.getLogin().equals(login) && user.getPassword().equals(pass)) {
                return user.getID();
            }
        }
        return -1;
    }

    @Override
    public String getUserFolder(int ID) {
        for (User user : users) {
            if (user.getID() == ID) {
                return user.getFolder();
            }
        }
        return null;
    }
}
