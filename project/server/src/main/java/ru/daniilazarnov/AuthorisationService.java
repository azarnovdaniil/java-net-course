package ru.daniilazarnov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;


public class AuthorisationService {

    private static DataBaseProcessor dBconnector;
    private static LinkedList<UserProfile> userList;
    private static Semaphore semaphore;
    private static final Logger LOGGER = LogManager.getLogger(AuthorisationService.class);

    public static void initAuthorisationService(LinkedList<UserProfile> userList) {
        dBconnector = new DataBaseProcessor();
        AuthorisationService.userList = userList;
        semaphore = new Semaphore(1);
    }

    public static void login(String login, String password, UserProfile user) {
        LOGGER.info("Login attempt.");
        try {
            semaphore.acquire();
            String result = dBconnector.userCheck(login, password);
            if (result.isBlank()) {
                user.getContextData().setCommand(CommandList.login.getNum());
                user.getCurChannel().writeAndFlush("false%%%No user found with your parameters".getBytes());
            } else if (isConnected(login)) {
                user.getContextData().setCommand(CommandList.login.getNum());
                user.getCurChannel().writeAndFlush("false%%%This user is already connected!".getBytes());
            } else {
                user.setLogin(login);
                user.setAuthority(ServerConfigReader.getRepoPath() + "\\" + result);
                ServerConfigReader.checkDir(user.getAuthority());
                user.getContextData().setCommand(CommandList.login.getNum());
                user.getCurChannel().writeAndFlush("true%%%Welcome back!".getBytes());
            }
            semaphore.release();
            LOGGER.info("Login attempt complete.");
        } catch (InterruptedException e) {
            LOGGER.error("SWW with register attempt", LOGGER.throwing(e));
        }
    }

    public static void register(String login, String password, UserProfile user) {
        LOGGER.info("Register attempt.");
        try {
            semaphore.acquire();
            String authority = login + File.separator;
            String result = dBconnector.createUser(login, password, authority);
            switch (result) {
                case "failed":
                    user.getContextData().setCommand(CommandList.register.getNum());
                    user.getCurChannel().writeAndFlush("false%%%Register failed. Please, try again later.".getBytes());
                    break;
                case "done":
                    user.setLogin(login);
                    user.setAuthority(Paths.get(ServerConfigReader.getRepoPath(), authority).toString());
                    ServerConfigReader.checkDir(user.getAuthority());
                    user.getContextData().setCommand(CommandList.register.getNum());
                    user.getCurChannel().writeAndFlush("true%%%Register complete. Welcome aboard!".getBytes());

                    break;
                case "parameters occupied":
                    user.getContextData().setCommand(CommandList.register.getNum());
                    user.getCurChannel().writeAndFlush("false%%%This login is occupied. Please, try another one".getBytes());
                    break;
            }
            semaphore.release();
            LOGGER.info("Register attempt complete.");
        } catch (InterruptedException e) {
            LOGGER.error("SWW with register attempt", LOGGER.throwing(e));
        }
    }

    private static boolean isConnected(String login) {
        for (UserProfile a : userList) {
            if (a.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

}
