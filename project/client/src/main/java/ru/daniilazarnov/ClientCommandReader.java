package ru.daniilazarnov;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class ClientCommandReader extends CommandReader{
    private ClientPathHolder pathHolder;


    ClientCommandReader(ContextData messageContext,ClientPathHolder pathHolder){
        super(messageContext);
        this.pathHolder=pathHolder;
    }


    @Override
    public void run() {

        if (super.messageContext.getCommand()==CommandList.login.ordinal()){
            String result = readServerMessage();
            if (result.equals("false")){
                Client.setIsAuthorised(false);
            }else if (result.equals("true")){
                Client.setIsAuthorised(true);
            }
        }else if (super.messageContext.getCommand()== CommandList.register.ordinal()){
            String result = readServerMessage();
            if (result.equals("false")){
                Client.setIsAuthorised(false);
            }else if (result.equals("true")){
                Client.setIsAuthorised(true);
            }
        }else if (super.messageContext.getCommand()== CommandList.getFileList.ordinal()){
            String fileList = new String(super.messageContext.getContainer());
            if(fileList.isEmpty()){
                System.out.println("Your storage is empty.");
                return;
            }
            String [] files = fileList.split("###");
            System.out.println("Your cloud repo contains:");
            Arrays.stream(files).forEach(System.out::println);
        }else if (super.messageContext.getCommand()== CommandList.serverMessage.ordinal()){
            readServerMessage();
        }else if(super.messageContext.getCommand()== CommandList.fileUploadInfo.ordinal()){
            pathHolder.setFileLength(Long.parseLong(super.messageContext.getPassword()));
            File incoming = Paths.get(Client.getRepoPath()+"\\"+super.messageContext.getFilePath()).toFile();
            if (incoming.exists()) {
                try {
                    Files.delete(incoming.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            System.out.println("Command unknown");
        }
    }

    private String readServerMessage(){
        String message = new String(super.messageContext.getContainer());
        String[] mesSet = message.split("%%%");
        System.out.println(mesSet[1]);
        return mesSet[0];
        }
    }

