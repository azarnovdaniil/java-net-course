package ru.sviridovaleksey.clientHandler;

import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.commands.AuthCommandData;
import ru.sviridovaleksey.workwithfiles.WorkWithFile;

public class RegistrationProcess {

    private DataBaseUser dataBaseUser;
    private WorkWithFile workWithFile;

    public RegistrationProcess (DataBaseUser dataBaseUser) {
        this.dataBaseUser = dataBaseUser;

    }

    public String isAuthOk (Command command) {

        AuthCommandData data = (AuthCommandData) command.getData();
        String login = data.getLogin();
        String password = data.getPassword();

        if (dataBaseUser.getUserNameByLogin(login, password) == null) {
            return null;
        } else {
            return login;
        }
    }


}
