package chat.server;

//import com.mysql.jdbc.Driver
//import helpers.DataBaseHelper;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationService {
    private Set<CredentialsEntry> entries;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public AuthenticationService() {
        update();
        logger.log(Level.INFO, "AuthenticationService is started");
    }

    public void update() {
        //entries = DataBaseHelper.getUsers();
        entries = Set.of(
                new CredentialsEntry("l1", "p1", "nickname1"),
                new CredentialsEntry("l2", "p2", "nickname2"),
                new CredentialsEntry("l3", "p3", "nickname3")
        );
    }

    public String findNicknameByLoginAndPassword(String login, String password) {
        for (CredentialsEntry entry : entries) {
            if (entry.getLogin().equals(login) && entry.getPassword().equals(password)) {
                return entry.getNickname();
            }
        }
        return null;
    }

    public static class CredentialsEntry {
        private String login;
        private String password;
        private String nickname;

        public CredentialsEntry(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }


        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
