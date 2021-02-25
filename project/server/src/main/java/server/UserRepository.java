package server;

import common.service.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {

    private Map<String, User> userMap = new ConcurrentHashMap<>();

    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    public User findByLogin(String login) {
        return userMap.get(login);
    }

    public boolean insert(User user) {
        if (user.getLogin() != null) {
            userMap.put(user.getLogin(), user);
            return true;
        }
        return false;
    }

    public void update(User user) {
        userMap.put(user.getLogin(), user);
    }

    public void delete(String login) {
        userMap.remove(login);
    }

    public boolean checkPassword(User user) {
        User userData = userMap.get(user.getLogin());
        return userData.getPassword().equals(user.getPassword());
    }
}
