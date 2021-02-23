package ru.atoroschin.auth;

import ru.atoroschin.Credentials;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseAuthService implements AuthService {

    private final List<User> users = List.of(
            new User("user1", "1", "user_1", 1),
            new User("user2", "1", "user_2", 10),
            new User("user3", "1", "user_3", 100));

    private final Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getLogin, user -> user));

    @Override
    public boolean isAuth(Credentials credentials) {
        if (userMap.containsKey(credentials.getLogin())) {
            User user = userMap.get(credentials.getLogin());
            return user.isAuth(credentials);
        }
        return false;
    }

    @Override
    public String getUserFolder(String login) throws IllegalAccessException {
        User user = userMap.get(login);
        if (user == null) {
            throw new IllegalAccessException();
        }
        return userMap.get(login).getFolder();
    }

    @Override
    public int getMaxVolume(String login) throws IllegalAccessException {
        User user = userMap.get(login);
        if (user == null) {
            throw new IllegalAccessException();
        }
        return userMap.get(login).getMaxVolume();
    }
}
