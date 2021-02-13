package ru.uio.io;

import ru.uio.io.handlers.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import static ru.uio.io.ClientHandlerEnum.*;

public class ClientApp {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8888);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            AtomicBoolean isClose = new AtomicBoolean();
            Map<ClientHandlerEnum, BaseHandler> handlerMap = new HashMap<>();
            handlerMap.put(AUTH, new AuthHandler(in, out));
            handlerMap.put(UPLOAD, new UploadHandler(in, out));
            handlerMap.put(LIST, new ListHandler(in, out));
            handlerMap.put(DOWNLOAD, new DownloadHandler(in, out));
            handlerMap.put(HELP, new HelpHandler(in, out));
            BaseHandler baseHandler;
            isClose.set(false);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                try {
                    if (isClose.get()){
                        System.out.println("Connect closed");
                        scanner.close();
                        break;
                    }
                    System.out.println("Enter command:");
                    String command = scanner.nextLine();
                    if (command.startsWith("-help")){
                        execute(handlerMap.get(HELP));
                        continue;
                    }

                    if (command.startsWith("-auth")){
                        baseHandler = handlerMap.get(AUTH);
                        baseHandler.setCommand(command);
                        execute(baseHandler);
                        continue;
                    }
                    if (command.startsWith("-u") || command.startsWith("-upload")){
                        System.out.println("upload");
                        execute(handlerMap.get(UPLOAD));
                        continue;
                    }
                    if (command.startsWith("-l") || command.startsWith("-list")){
                        execute(handlerMap.get(LIST));
                        continue;
                    }
                    if (command.startsWith("-d") || command.startsWith("-download")){
                        String[] str = command.split(" ");
                        if(str.length < 1){
                            System.out.println("Please write number file. Like: -d 1");
                            continue;
                        }
                        baseHandler = handlerMap.get(DOWNLOAD);
                        baseHandler.setCommand(str[1]);
                        execute(baseHandler);
                        continue;
                    }
                    if (command.startsWith("-exit")){
                        out.write(Common.createDataPacket("120".getBytes("UTF8"), "exit".getBytes("UTF8")));
                        out.flush();
                        isClose.set(true);
                    }

                } catch (IOException e) {
                    throw new RuntimeException("SWW6", e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("SWW10", e);
        }
    }

    private static void execute(BaseHandler baseHandler){
        baseHandler.workWithMessage();
    }
}
