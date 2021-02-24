package ru.daniilazarnov.auth;
import ru.daniilazarnov.User;
import java.util.ArrayList;

public class BaseAuthService implements AuthService {

    private static final ArrayList<User> users = new ArrayList<User>();

    public String getUsernameByLoginAndPassword(String login, String password) {
        User u1 = new User("bob", "bob", "Bob");
        User u2 = new User("jon", "jon", "Jon");
        User u3 = new User("sam", "sam", "Sam");
        users.add(u1);
        users.add(u2);
        users.add(u3);

        for (User user : users) {
            if(user.getLogin().equals(login) & user.getPassword().equals(password) ) {
                return user.getUsername();
            }
        }
        return null;
    }
}
