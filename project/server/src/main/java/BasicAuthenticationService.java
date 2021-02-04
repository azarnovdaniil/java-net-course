

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BasicAuthenticationService implements AuthenticationService{
    private static final List <User> users; //БД

    static {
        users = Arrays.asList(
                new User("n1","n1@mail.com", "1"),
                new User("n2","n2@mail.com", "2"),
                new User("n3","n3@mail.com", "3")
        );
    }

    @Override
    public Optional <User> doAuth(String email, String password) {
        for (User user: users){
            if (user.getEmail().equals(email) && user.getPassword().equals(password)){
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}