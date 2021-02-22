package ru.atoroschin.auth;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseAuthService implements AuthService {

    private final List<User> users = List.of(new User(1, "user1", "1", "user_1", 1),
            new User(2, "user2", "1", "user_2", 1),
            new User(3, "user3", "1", "user_3", 1),
            new User(-1, "unknown", "", "unknown", 1));

    private final Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getLogin, user -> user));

    @Override
    public boolean isAuth(String login, String pass) {
        for (User user : users) {
            if (user.getLogin().equals(login) && user.getPassword().equals(pass)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getUserFolder(String login) {
        User user = userMap.get(login);
        if (user == null) {
//            throw new IllegalAccessException();
        }
        return userMap.get(login).getFolder();
    }

    @Override
    public int getMaxVolume(String login) {
        for (User user : users) {
            if (user.getLogin().equals(login)) {
                return user.getMaxVolume();
            }
        }
        return 0;
    }
}
