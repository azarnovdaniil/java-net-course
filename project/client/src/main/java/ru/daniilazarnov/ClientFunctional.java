package ru.daniilazarnov;

public class ClientFunctional {
    public void getInfo(){
        for (Command cmd : Command.values()){
            System.out.println(cmd.getCommand() + cmd.getDescription());
        }
    }
}
