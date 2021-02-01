package server.chat.auth;

import server.chat.User;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class BaseAuthService {
    private DataBaseService dbService;

    //private static final List<User> users = List.of(
    private static final List<User> users = Arrays.asList(
            new User("Александр", "user1", "111", "nik1"),
            new User("Даниил", "user2", "222", "nik2"),
            new User("Евгений", "user3", "333", "nik3")
    );

    public BaseAuthService() {
        dbService = new DataBaseService();
    }

    public void close() {
        dbService.disconnect();
    }

    public String getUserName(String login, String password) {
        try {
            return dbService.getNameByLoginPass(login, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
//        for (User user : users) {
//            if (user.getLogin().equals(login)&&user.getPassword().equals(password)) {
//                return user.getName();
//            }
//        }
    }

    public String getNickNameByLogin(String login) {
        for (User user : users) {
            if (user.getLogin().equals(login)) {
                return user.getNickName();
            }
        }
        return null;
    }

    public boolean changeLogin(String login, String loginNew) {
        return dbService.changeLogin(login, loginNew);
    }
}
