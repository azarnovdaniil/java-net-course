package ru.daniilazarnov;

import java.util.LinkedList;


public class AuthorisationService {

   private static DataBaseProcessor DBconnector;
   private static LinkedList<UserProfile> userList;

    AuthorisationService(LinkedList<UserProfile> userList){
        DBconnector = new DataBaseProcessor();
        AuthorisationService.userList =userList;
    }

    public static void login(String login, String password, UserProfile user){
        String result = DBconnector.userCheck(login,password);
        if (result.isBlank()){
            user.getContextData().setCommand(CommandList.login.getNum());
            user.getCurChannel().writeAndFlush("false%%%No user found with your parameters".getBytes());
        }else if (isConnected(login)){
                user.getContextData().setCommand(CommandList.login.getNum());
                user.getCurChannel().writeAndFlush("false%%%This user is already connected!".getBytes());
        }else {
            user.setLogin(login);
            user.setAuthority(Server.getRepoPath()+"\\"+result);
            Server.checkDir(user.getAuthority());
            user.getContextData().setCommand(CommandList.login.getNum());
            user.getCurChannel().writeAndFlush("true%%%Welcome back!".getBytes());
        }
    }

    public static void register(String login, String password, UserProfile user){
        String authority = login+"\\";
        String result = DBconnector.createUser(login,password,authority);
        if (result.equals("failed")){
            user.getContextData().setCommand(CommandList.register.getNum());
            user.getCurChannel().writeAndFlush("false%%%Register failed. Please, try again later.".getBytes());
        }else if (result.equals("done")){
            user.setLogin(login);
            user.setAuthority(Server.getRepoPath()+"\\"+authority);
            Server.checkDir(user.getAuthority());
            user.getContextData().setCommand(CommandList.register.getNum());
            user.getCurChannel().writeAndFlush("true%%%Register complete. Welcome aboard!".getBytes());

        }else if (result.equals("parameters occupied")){
            user.getContextData().setCommand(CommandList.register.getNum());
            user.getCurChannel().writeAndFlush("false%%%This login is occupied. Please, try another one".getBytes());
        }
    }

    private static boolean isConnected(String login){
        for (UserProfile a: userList) {
            if(a.getLogin().equals(login)){
                return true;
            }
        }
        return false;
    }

}
