package common.Services;
import java.io.Serializable;
public class AuthCommandService implements Serializable
{
    private final String login;
    private final String password;

    public AuthCommandService(String login, String password)
    {
        this.password = password;
        this.login = login;

    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }


}
