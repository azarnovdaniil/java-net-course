package ru.daniilazarnov;

public abstract class CommandReader implements Runnable{

    ContextData messageContext;

    CommandReader (ContextData messageContext){
        this.messageContext=messageContext;
    }
    @Override
    public void run() {

    }
}
