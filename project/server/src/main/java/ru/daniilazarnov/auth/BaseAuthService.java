package ru.daniilazarnov.auth;

import ru.daniilazarnov.User;

import java.util.ArrayList;
import java.util.List;


public class BaseAuthService implements AuthService {

    private static final ArrayList<User> clients = new ArrayList<User>();
    // метод .of не работает:
    //ArrayList.of(
    //new User("user1", "1111", "Борис"),
    //new User("user2", "2222", "Тимофей"),
    //new User("user3", "3333", "Мартин")
    //);

    public String getUsernameByLoginAndPassword(String login, String password) {
        User u1 = new User("user1", "1111", "Борис");
        User u2 = new User("user2", "2222", "Тимофей");
        User u3 = new User("user3", "3333", "Мартин");
        clients.add(u1);
        clients.add(u2);
        clients.add(u3);

        for (User client : clients) {
            if(client.getLogin().equals(login) & client.getPassword().equals(password) ) {
                return client.getUsername();
            }
        }
        return null;
    }
}
