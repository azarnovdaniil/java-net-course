import com.chat.entity.User;

import java.util.Optional;

public interface AuthenticationService {
    Optional <User> doAuth (String login, String password);
}
