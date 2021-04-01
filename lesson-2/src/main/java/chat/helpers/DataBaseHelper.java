package chat.helpers;

import chat.server.AuthenticationService;
import chat.server.ConnectionService;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DataBaseHelper {

    private static final String SELECT_ALL_USERS = "select login, password, nickname from chat.users";
    private static final String UPDATE_USER_NICKNAME = "update chat.users set nickname = ? where nickname = ?";

    public DataBaseHelper() {
    }

    //пробую кучу autocloseable через try
    public static Set<AuthenticationService.CredentialsEntry> getUsers() {
        Set<AuthenticationService.CredentialsEntry> result = new HashSet<>();

        try (Connection con = ConnectionService.connect()) {
            try (Statement s = con.createStatement()) {
                s.execute(SELECT_ALL_USERS);

                try (ResultSet rs = s.getResultSet()) {
                    while (rs.next()) {
                        result.add(new AuthenticationService.CredentialsEntry(
                                rs.getString("login"),
                                rs.getString("password"),
                                rs.getString("nickname")
                        ));
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public static boolean updateNickname(String oldNick, String newNick) {
        boolean result = false;
        Connection con = ConnectionService.connect();

        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(UPDATE_USER_NICKNAME);
            ps.setString(1, newNick);
            ps.setString(2, oldNick);

            if (ps.executeUpdate() > 0) {
                con.commit();
                result = true;
            }
        } catch (SQLException throwables) {
            ConnectionService.rollback(con);
            throwables.printStackTrace();
        } finally {
            ConnectionService.close(con);
        }

        return result;
    }
}
