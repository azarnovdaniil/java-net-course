package ru.atoroschin.auth;

import ru.atoroschin.AuthService;

import java.util.List;

public class BaseAuthService implements AuthService {

    private final List<User> users = List.of(new User(1, "user1", "1", "user_1", 1),
            new User(2, "user2", "1", "user_2", 1),
            new User(3, "user3", "1", "user_3", 1),
            new User(-1, "unknown", "", "unknown", 1));

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
    public String getUserFolder(int id) {
        for (User user : users) {
            if (user.getID() == id) {
                return user.getFolder();
            }
        }
        return null;
    }

    @Override
    public int getMaxVolume(int id) {
        for (User user : users) {
            if (user.getID() == id) {
                return user.getMaxVolume();
            }
        }
        return 0;
    }
}
