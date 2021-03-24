package ru.daniilazarnov;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Consumer;

public class Console {

    private final RepoClient connection;
    private final Consumer<ContextData> context;
    public ContextData data;


    Console (RepoClient connection, Consumer <ContextData> context){
        this.connection = connection;
        this.context = context;
    }

    public void start (){
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Please enter command. Print help for command list.");
            commandDecoder(scanner.nextLine());

        }
    }

    public void commandDecoder (String command) {
        String [] comArray = command.split(" ");

        try {
            switch (CommandList.valueOf(comArray[0])) {
                case help:
                    if (comArray.length==1) {
                        System.out.println(Arrays.asList(CommandList.values()));
                        System.out.println("If you need more details, print help command_you_need to know more about it.");
                    } else help(comArray[1]);
                    break;
                case login:
                    data = new ContextData();
                    data.setCommand(CommandList.login.ordinal());
                    data.setLogin(comArray[1]);
                    data.setPassword(comArray[2]);
                    context.accept(data);
                    break;
                case register:
                    data = new ContextData();
                    data.setCommand(CommandList.register.ordinal());
                    data.setLogin(comArray[1]);
                    data.setPassword(comArray[2]);
                    context.accept(data);
                    break;
                case getFileList:
                    data = new ContextData();
                    data.setCommand(CommandList.getFileList.ordinal());
                    context.accept(data);
                    break;
                case upload:
                    String path;
                    if (comArray[1].startsWith("\\")){
                        path = Client.getRepoPath()+comArray[1];
                    }else path=comArray[1];
                    Path fileToSend = Paths.get(path);
                    if (fileToSend.toFile().exists()) {
                        data = new ContextData();
                        data.setCommand(CommandList.upload.ordinal());
                        data.setFilePath(fileToSend.toString());
                        context.accept(data);
                    }else System.out.println("File not found. Check the path.");
                    break;
                case download:
                    data = new ContextData();
                    data.setCommand(CommandList.download.ordinal());
                    data.setFilePath(comArray[1]);
                    context.accept(data);
                    break;
                case delete:
                    data = new ContextData();
                    data.setCommand(CommandList.delete.ordinal());
                    data.setFilePath(comArray[1]);
                    context.accept(data);
                    break;
                case setRepository:
                    Path repo = Paths.get(comArray[1]);
                    if (!repo.toFile().exists()) {
                        repo.toFile().mkdir();
                    }
                    if (repo.toFile().exists()) {
                        Client.setRepoPath(repo.toString());
                        Client.writeConfig();
                        System.out.println("Your new repository is: "+repo.toString());
                        break;
                    }
                    System.out.println("Invalid path!");
                    break;
                case exit:
                    System.out.println("See you later!");
                    this.connection.close();
                    System.exit(0);
                    break;
                case connect:
                    System.out.println("Trying to connect...");
                    this.connection.start();
                    break;
                case setHost:
                    Client.setHost(comArray[1]);
                    int port = Integer.parseInt(comArray[2]);
                    Client.setPort(port);
                    if(Client.getPort()==port && Client.getHost().equals(comArray[1])){
                        Client.writeConfig();
                        System.out.println("New host and port are set");
                    }
                    break;

            }
        }catch (RuntimeException e){
            System.out.println("No such command found. Print help to get command list");
        }

    }

    public void help (String command) {
        System.out.println(command+" details...");
    }

}
