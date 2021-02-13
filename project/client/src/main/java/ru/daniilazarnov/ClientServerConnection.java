package ru.daniilazarnov;

import javafx.application.Platform;
import ru.daniilazarnov.CommandsType.ErrorCommandData;


import java.io.*;
import java.net.Socket;

public class ClientServerConnection {

    private Socket socket;
    private ObjectInputStream dataInputStream;
    private ObjectOutputStream dataOutputStream;


    public ObjectOutputStream getDataOutputStream() {

        return dataOutputStream;
    }

    public ObjectInputStream getDataInputStream() {

        return dataInputStream;
    }



    public boolean startConnect(){
        try{
            Socket socket = new Socket("localhost", 8189);
            dataInputStream = new ObjectInputStream(socket.getInputStream());
            dataOutputStream = new ObjectOutputStream(socket.getOutputStream());
            return true;

        } catch (IOException e) {
            System.out.println("Connection lost...");
            e.printStackTrace();
            return false;
        }
    }

    public void OptionPanel(OptionsController optionsController){

        Thread thread = new Thread( () -> {
            try {
                while (true) {

                    Commands commands = executeCommands();
                    if (commands == null) {
                        optionsController.showError("Server Error", "Incorrect command");
                        continue;
                    }
                    switch (commands.getType()) {
                        case ERROR: {
                            ErrorCommandData data = (ErrorCommandData) commands.getData();
                            String errorMessage = data.getErrorMessage();
                            Platform.runLater(() -> {
                                optionsController.showError("Server error", errorMessage);

                            });
                            break;
                        }
                        case DELETE_FILES:

                    default:
                        Platform.runLater(() -> {
                            optionsController.showError("Unknown command from server!", commands.getType().toString());
                        });
                }

            }
                }catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Connection lost!");
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }



    private Commands executeCommands() throws IOException{

        try{
            return (Commands) dataInputStream.readObject();
        } catch (ClassNotFoundException e){
            String errorMessage = "Incorrect data!";
            System.err.println(errorMessage);
            e.printStackTrace();
            sendMessage(Commands.errorCommand(errorMessage));
            return null;
        }
    }

    private void sendMessage(Commands commands) throws IOException{

        dataOutputStream.writeObject(commands);
    }

    public void stopConnect(){
        try{
           socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
